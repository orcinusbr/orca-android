/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.notification.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.service.notification.StatusBarNotification
import androidx.annotation.VisibleForTesting
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.service.security.Locksmith
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.app
import com.google.firebase.initialize
import io.ktor.client.statement.bodyAsText
import io.ktor.util.StringValuesBuilder
import java.net.URI
import java.net.URISyntaxException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * [Service] that subscribes to the receipt of updates regarding the authenticated [Actor] from the
 * Mastodon server and forwards them to their device via system notifications. Each notification
 * type defined by the Mastodon documentation as of v1 ("favourite", "follow", "follow request",
 * "mention", "poll", "reblog", "severed relationships", "status" and "update") will have a channel
 * of its own when their notifications are sent.
 *
 * Note that, upon binding, a valid endpoint to which these server updates are forwarded is
 * *required* to have been specified as an extra in the [Intent]. On the contrary, one of the two
 * will be thrown: an [URISyntaxException] in case one has been provided but is invalid; or an
 * [IllegalStateException] if none is given at all.
 *
 * @property requester [Requester] by which push subscriptions are performed.
 * @property authenticationLock [AuthenticationLock] for requiring an authenticated [Actor] when
 *   converting received payloads into system notifications.
 * @property locksmith [Locksmith] by which the keys for encryption and decryption of received
 *   updates are provided.
 * @see MastodonNotification.Type
 * @see MastodonNotification.Type.toNotificationChannel
 * @see Intent.putExtra
 */
@InternalNotificationApi
internal class NotificationService
@VisibleForTesting
constructor(
  private val requester: Requester,
  private val authenticationLock: SomeAuthenticationLock,
  private val locksmith: Locksmith
) : Service() {
  /**
   * [Binder] for interprocess communication (IPC) returned by [onBind]. Instantiated upon creation
   * and nullified upon destruction.
   *
   * @see onCreate
   * @see onDestroy
   */
  private var binder: Binder? = null

  /**
   * [String] form of the [URI] to which server updates are forwarded, obtained from the [Intent]
   * with which a connection to this [NotificationService] was bound. Guaranteed to be a valid URL
   * if not `null`.
   */
  private var endpoint: String? = null
    @Throws(URISyntaxException::class)
    set(endpoint) {
      endpoint?.also(::URI)
      field = endpoint
    }

  /**
   * IDs of [Notification]s that have been sent and have not yet been cleared by this
   * [NotificationService].
   *
   * @see StatusBarNotification.getId
   */
  private val sentNotificationIds = mutableSetOf<Int>()

  /**
   * [CoroutineScope] in which remotely received payloads are converted into [MastodonNotification]
   * DTOs and requests to subscribe to the receipt of notifications from the Mastodon server are
   * sent.
   *
   * @see sendLastNotification
   * @see subscribe
   */
  @VisibleForTesting
  var coroutineScope = CoroutineScope(Dispatchers.Default)
    private set

  /**
   * Current [Lifecycle.State] in which this [NotificationService] is, constrained to representing
   * only _initialized_, _created_ or _destroyed_ states. As of now, it has no actual use in
   * production and exists only for testing purposes.
   *
   * @see Lifecycle.State.INITIALIZED
   * @see Lifecycle.State.CREATED
   * @see Lifecycle.State.DESTROYED
   */
  @VisibleForTesting
  var lifecycleState = Lifecycle.State.INITIALIZED
    private set

  /**
   * [NotificationManager] through which updates received from the server will be forwarded in the
   * form of system notifications.
   */
  @VisibleForTesting
  val notificationManager
    get() = getSystemService<NotificationManager>() as NotificationManager

  /**
   * Whether the Firebase SDK is currently running.
   *
   * @see startFirebaseSdk
   * @see stopFirebaseSdkIfRunning
   */
  @VisibleForTesting
  val isFirebaseSdkRunning
    get() =
      try {
        Firebase.app
        true
      } catch (_: IllegalStateException) {
        false
      }

  @Throws(
    Injector.ModuleNotRegisteredException::class,
    Module.DependencyNotInjectedException::class
  )
  constructor() :
    this(
      requester = Injector.get(),
      Injector.from<CoreModule>().authenticationLock(),
      Locksmith()
    ) {
    setCoroutineContext(Dispatchers.IO)

    // TODO: Deject this listener upon destruction.
    Injector.injectLazily<OnMessageReceiptListener> { ::sendLastNotification }
  }

  override fun onCreate() {
    super.onCreate()
    lifecycleState = Lifecycle.State.CREATED
    binder = Binder()
    startFirebaseSdkIfNotRunning()
    subscribe()
  }

  @Throws(IllegalStateException::class, URISyntaxException::class)
  override fun onBind(intent: Intent?): IBinder? {
    endpoint = checkNotNull(intent?.endpoint) { "No endpoint specified." }
    return binder
  }

  override fun onDestroy() {
    super.onDestroy()
    cancelSentNotifications()
    coroutineScope.cancel("Service has been destroyed.")
    stopFirebaseSdkIfRunning()
    lifecycleState = Lifecycle.State.DESTROYED
    binder = null
  }

  /**
   * Changes the context in which subscription pushes and notification DTO conversions into system
   * notifications are performed in the [coroutineScope]. All active [Job]s (if any) are cancelled
   * when this method is called.
   *
   * @param coroutineContext [CoroutineContext] to switch to and run suspending operations in.
   * @see Job.isActive
   */
  @VisibleForTesting
  fun setCoroutineContext(coroutineContext: CoroutineContext) {
    coroutineScope.cancel("Service coroutine context is being changed.")
    coroutineScope = CoroutineScope(coroutineContext + Job(parent = coroutineContext[Job]))
  }

  /**
   * Appends form data for the `application/x-www-form-urlencoded`-MIME-typed subscription request
   * to the [builder], defining that all updates should be received from the Mastodon server and
   * forwarded to the device.
   *
   * @param builder [StringValuesBuilder] to which the data is to be appended.
   * @see subscribe
   */
  @VisibleForTesting
  fun appendSubscriptionFormData(builder: StringValuesBuilder) {
    builder.apply {
      append("data[alerts][admin.report]", "true")
      append("data[alerts][admin.sign_up]", "true")
      append("data[alerts][favourite]", "true")
      append("data[alerts][follow]", "true")
      append("data[alerts][follow_request]", "true")
      append("data[alerts][mention]", "true")
      append("data[alerts][poll]", "true")
      append("data[alerts][reblog]", "true")
      append("data[alerts][status]", "true")
      append("data[alerts][update]", "true")
      append("data[policy]", "all")
      endpoint?.let { append("subscription[endpoint]", it) }
      append("subscription[keys][auth]", locksmith.authenticationKey)
      append("subscription[keys][p256dh]", locksmith.publicKey)
    }
  }

  /**
   * Suspends until the specified system notification gets sent.
   *
   * @param id ID of the [Notification] whose delivery will be synchronously awaited.
   * @see StatusBarNotification.getId
   */
  @VisibleForTesting
  suspend fun awaitUntilSent(id: Int) {
    suspendCoroutine {
      @Suppress("ControlFlowWithEmptyBody") while (hasNotBeenSent(id)) {}
      it.resume(Unit)
    }
  }

  /**
   * Starts the Firebase SDK in case it has not been started yet.
   *
   * @see startFirebaseSdk
   * @see isFirebaseSdkRunning
   */
  private fun startFirebaseSdkIfNotRunning() {
    if (!isFirebaseSdkRunning) {
      startFirebaseSdk()
    }
  }

  /**
   * Starts the Firebase SDK manually with the respective project info and API key.
   *
   * This approach is preferred because the
   * [`google-services.json`-based one](https://firebase.google.com/docs/android/setup?hl=pt-br#add-config-file)
   * requires the application of a plugin and the addition of such file — both in the `:app` module.
   * Doing so, however, would propagate the responsibility of configuring the Firebase SDK, which
   * belongs only to the Mastodon-specific core.
   */
  private fun startFirebaseSdk() {
    FirebaseOptions.Builder()
      .setApiKey(BuildConfig.mastodonfirebaseApiKey)
      .setApplicationId(BuildConfig.mastodonfirebaseApplicationID)
      .setProjectId(BuildConfig.mastodonfirebaseProjectID)
      .setStorageBucket(BuildConfig.mastodonfirebaseStorageBucket)
      .build()
      .let { Firebase.initialize(this@NotificationService, /* options = */ it) }
      .apply { @Suppress("DEPRECATION") setDataCollectionDefaultEnabled(false) }
  }

  /**
   * Verifies whether the specified [Notification] has _not_ been sent.
   *
   * @param id ID of the [Notification] whose current absence of delivery will be checked.
   */
  private fun hasNotBeenSent(id: Int): Boolean {
    return notificationManager.activeNotifications
      ?.map(StatusBarNotification::getId)
      ?.contains(id)
      ?.not()
      ?: true
  }

  /** Subscribes to the receipt of updates from the Mastodon server. */
  private fun subscribe() {
    coroutineScope.launch {
      requester.authenticated().post(HostedURLBuilder::buildNotificationSubscriptionPushingRoute) {
        parameters(::appendSubscriptionFormData)
      }
    }
  }

  /**
   * Sends a system notification based on the last one received from the Mastodon server. As
   * converting a DTO into a system notification is a suspending operation, it is sent in the
   * [coroutineScope]; thus, this method will probably return _before_ the notification is delivered
   * to the device (this is specially important to keep in mind for testing scenarios).
   *
   * ###### Implementation notes
   *
   * Sending the notification received in the actual message instead of requesting the last one is a
   * more straightforward approach, but also a complex one — in the official source code, there are
   * the AES/GCM-decrypting and the extended-Z85-decoding phases, which would require Orca to test
   * both of them and, thus, compute encrypted and encoded inputs and test encryption and encoding
   * themselves.
   *
   * While not ideal, performing a request to fetch the last notification seems to be the plausible
   * solution for now.
   *
   * @see OnMessageReceiptListener
   */
  private fun sendLastNotification() {
    coroutineScope.launch {
      val dto = getLastNotificationDto()
      val channel = dto.type.toNotificationChannel(this@NotificationService)
      val id = dto.generateSystemNotificationID()
      val notification = dto.toNotification(this@NotificationService, authenticationLock)
      notificationManager.createNotificationChannel(channel)
      notificationManager.notify(id, notification)
      sentNotificationIds += id
    }
  }

  /** Obtains the DTO of the notification received lastly from the Mastodon server. */
  private suspend fun getLastNotificationDto(): MastodonNotification {
    return requester
      .authenticated()
      .get(HostedURLBuilder::buildNotificationsRoute) { headers { append("limit", "1") } }
      .bodyAsText()
      .let { Json.decodeFromString(dtosSerializer, it) }
      .last()
  }

  /** Cancels all system notifications that have been sent by this [NotificationService]. */
  private fun cancelSentNotifications() {
    for (sentNotificationID in sentNotificationIds) {
      notificationManager.cancel(sentNotificationID)
    }
    sentNotificationIds.clear()
  }

  /**
   * Deletes the default [FirebaseApp] in case it is currently running.
   *
   * @see isFirebaseSdkRunning
   */
  private fun stopFirebaseSdkIfRunning() {
    if (isFirebaseSdkRunning) {
      Firebase.app.delete()
    }
  }

  companion object {
    /**
     * Key that identifies the endpoint [String] extra in an [Intent].
     *
     * @see createIntent
     */
    private const val ENDPOINT_EXTRA_KEY = "endpoint"

    /** [KSerializer] that serializes the response to a request for obtaining notifications. */
    @JvmField
    @VisibleForTesting
    val dtosSerializer = ListSerializer(MastodonNotification.Serializer.instance)

    /**
     * Endpoint put into this [Intent] as an extra.
     *
     * @see NotificationService.endpoint
     * @see Intent.putExtra
     */
    @JvmStatic
    @VisibleForTesting
    val Intent.endpoint
      get() = getStringExtra(ENDPOINT_EXTRA_KEY)

    /**
     * Binds a connection to a [NotificationService] if none has been bound yet.
     *
     * @param context [Context] in which the binding is performed.
     * @param intent [Intent] with which the connection is to be bound.
     * @see createIntent
     */
    @JvmStatic
    fun bind(context: Context, intent: Intent): ServiceConnection {
      val connection = NoOpServiceConnection()
      var flags = Context.BIND_AUTO_CREATE
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        flags = flags or Context.BIND_NOT_PERCEPTIBLE
      }
      context.bindService(intent, connection, flags)
      return connection
    }

    /**
     * Creates an [Intent] with which a connection to a [NotificationService] is bound.
     *
     * @param context [Context] of the package in which the class is.
     * @param endpoint [String] of the [URI] to which server updates are forwarded.
     */
    @JvmStatic
    fun createIntent(context: Context, endpoint: String): Intent {
      return Intent(context, NotificationService::class.java).apply {
        putExtra(ENDPOINT_EXTRA_KEY, endpoint)
      }
    }
  }
}

/**
 * Builds a [URI] to which `POST` HTTP requests intended to push a subscription for receiving
 * [MastodonNotification]s are sent.
 */
@VisibleForTesting(VisibleForTesting.PACKAGE_PRIVATE)
internal fun HostedURLBuilder.buildNotificationSubscriptionPushingRoute(): URI {
  return path("api").path("v1").path("push").path("subscription").build()
}

/** Builds a [URI] to which `POST` HTTP requests for fetching notifications are sent. */
@VisibleForTesting(VisibleForTesting.PACKAGE_PRIVATE)
internal fun HostedURLBuilder.buildNotificationsRoute(): URI {
  return path("api").path("v1").path("notifications").build()
}

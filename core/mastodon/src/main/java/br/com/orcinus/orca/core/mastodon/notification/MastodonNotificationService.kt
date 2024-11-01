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

package br.com.orcinus.orca.core.mastodon.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
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
import br.com.orcinus.orca.core.mastodon.notification.security.Locksmith
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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.statement.bodyAsText
import io.ktor.util.StringValuesBuilder
import java.net.URI
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * [FirebaseMessagingService] that subscribes to the receipt of updates regarding the authenticated
 * [Actor] from the Mastodon server and forwards them to their device via system notifications. Each
 * notification type defined by the Mastodon documentation as of v1 ("favourite", "follow", "follow
 * request", "mention", "poll", "reblog", "severed relationships", "status" and "update") will have
 * a channel of its own when their notifications are sent.
 *
 * @property requester [Requester] by which push subscriptions are performed.
 * @property authenticationLock [AuthenticationLock] for requiring an authenticated [Actor] when
 *   converting received payloads into system notifications.
 * @property locksmith [Locksmith] by which the keys for encryption and decryption of received
 *   updates are provided.
 * @see MastodonNotification.Type
 * @see MastodonNotification.Type.toNotificationChannel
 */
internal class MastodonNotificationService
@VisibleForTesting
constructor(
  private val requester: Requester,
  private val authenticationLock: SomeAuthenticationLock,
  private val locksmith: Locksmith
) : FirebaseMessagingService() {
  /**
   * IDs of [Notification]s that have been sent and have not yet been cleared by this
   * [MastodonNotificationService].
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
   * @see pushSubscription
   */
  @VisibleForTesting
  var coroutineScope = CoroutineScope(Dispatchers.Default)
    private set

  /**
   * Current [Lifecycle.State] in which this [MastodonNotificationService] is, constrained to
   * representing only _initialized_, _created_ or _destroyed_ states. As of now, it has no actual
   * use in production and exists only for testing purposes.
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
  }

  override fun onCreate() {
    super.onCreate()
    startFirebaseSdkIfNotRunning()
    lifecycleState = Lifecycle.State.CREATED
  }

  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    sendLastNotification()
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    pushSubscription(token)
  }

  override fun onDestroy() {
    super.onDestroy()
    cancelSentNotifications()
    coroutineScope.cancel("Service has been destroyed.")
    stopFirebaseSdkIfRunning()
    lifecycleState = Lifecycle.State.DESTROYED
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
   * @param token Key that identifies each Firebase Cloud Messaging (FCM) client instance.
   * @see pushSubscription
   */
  @VisibleForTesting
  fun appendSubscriptionFormData(builder: StringValuesBuilder, token: String) {
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
      append("subscription[endpoint]", "https://fcm.googleapis.com/fcm/send/$token")
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
      .let { Firebase.initialize(this@MastodonNotificationService, /* options = */ it) }
      .apply { @Suppress("DEPRECATION") setDataCollectionDefaultEnabled(false) }
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
   * the AES/GCM-decrypting and the Base85-decoding phases, which would require Orca to test both of
   * them and, thus, compute encrypted and encoded inputs and test encryption and encoding
   * themselves. There has not been much of a success in my end in trying (for weeks) to
   * reverse-engineer how an input is to be encoded in the specific Base85 coding adopted by
   * Mastodon (which can be due to my limited cognitive ability).
   *
   * While not ideal, performing a request to fetch the last notification (done by
   * [getLastNotificationDto]) seems to be the plausible solution for now.
   */
  private fun sendLastNotification() {
    coroutineScope.launch {
      val dto = getLastNotificationDto()
      val channel = dto.type.toNotificationChannel(this@MastodonNotificationService)
      val id = dto.generateSystemNotificationID()
      val notification = dto.toNotification(this@MastodonNotificationService, authenticationLock)
      notificationManager.createNotificationChannel(channel)
      notificationManager.notify(id, notification)
      sentNotificationIds += id
    }
  }

  /** Obtains the DTO of the notification received lastly from the Mastodon server. */
  private suspend fun getLastNotificationDto(): MastodonNotification {
    return requester
      .authenticated()
      .get(HostedURLBuilder::buildNotificationsRoute)
      .bodyAsText()
      .let { Json.decodeFromString(ListSerializer(MastodonNotification.Serializer.instance), it) }
      .last()
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

  /**
   * Subscribes to the receipt of updates from the Mastodon server.
   *
   * @param token Key that identifies each Firebase Cloud Messaging (FCM) client instance.
   */
  private fun pushSubscription(token: String) {
    coroutineScope.launch {
      requester.authenticated().post(HostedURLBuilder::buildNotificationSubscriptionPushingRoute) {
        parameters { appendSubscriptionFormData(this, token) }
      }
    }
  }

  /** Cancels all system notifications that have been sent by this [MastodonNotificationService]. */
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
     * Binds a connection to a [MastodonNotificationService] if none has been bound yet.
     *
     * @param context [Context] in which the binding is performed.
     */
    @JvmStatic
    fun bind(context: Context) {
      val intent = Intent(context, MastodonNotificationService::class.java)
      var flags = Context.BIND_AUTO_CREATE
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        flags = flags or Context.BIND_NOT_PERCEPTIBLE
      }
      context.bindService(intent, NoOpServiceConnection, flags)
    }
  }
}

/**
 * Builds a [URI] to which `POST` HTTP requests intended to push a subscription for receiving
 * [MastodonNotification]s are sent.
 */
@VisibleForTesting
internal fun HostedURLBuilder.buildNotificationSubscriptionPushingRoute(): URI {
  return path("api").path("v1").path("push").path("subscription").build()
}

/** Builds a [URI] to which `POST` HTTP requests for fetching notifications are sent. */
@VisibleForTesting
internal fun HostedURLBuilder.buildNotificationsRoute(): URI {
  return path("api").path("v1").path("notifications").build()
}

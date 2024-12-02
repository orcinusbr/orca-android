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

package br.com.orcinus.orca.core.mastodon.notification.push

import android.app.Notification
import android.app.NotificationManager
import android.service.notification.StatusBarNotification
import androidx.annotation.VisibleForTesting
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.push.web.WebPush
import br.com.orcinus.orca.ext.uri.URIBuilder
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Subscribes to the receipt of updates regarding the authenticated [Actor] from the Mastodon server
 * and forwards them to their device via system notifications. Each notification type defined by the
 * Mastodon documentation as of v1 ("favourite", "follow", "follow request", "mention", "poll",
 * "reblog", "severed relationships", "status" and "update") will have a channel of its own when
 * their notifications are sent.
 *
 * @property requester [Requester] by which push subscriptions are performed.
 * @property webPush [WebPush] by which the keys for encryption and decryption of received updates
 *   are provided.
 * @see PushNotification.Type
 * @see PushNotification.Type.toNotificationChannel
 */
@InternalNotificationApi
internal class PushNotificationService
@VisibleForTesting
constructor(private val requester: Requester, private val webPush: WebPush) :
  FirebaseMessagingService() {
  /**
   * System notification IDs of push notifications that have been sent and have not yet been cleared
   * by this [PushNotificationService].
   *
   * @see PushNotification.generateSystemNotificationID
   */
  private val sentSystemPushNotificationIds = mutableSetOf<Int>()

  /**
   * [AuthenticationLock] for requiring an authenticated [Actor] when pushing a subscription and
   * forwarding server updates to the device as system notifications.
   */
  private inline val authenticationLock
    get() = requester.authenticated().lock

  /**
   * [CoroutineScope] in which remotely received payloads are converted into [PushNotification] DTOs
   * and requests to subscribe to the receipt of notifications from the Mastodon server are sent.
   *
   * @see sendLastPushNotification
   * @see subscribe
   */
  @VisibleForTesting
  var coroutineScope = CoroutineScope(Dispatchers.Default)
    private set

  /**
   * Current [Lifecycle.State] in which this [PushNotificationService] is, constrained to
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
  inline val notificationManager
    get() = getSystemService<NotificationManager>() as NotificationManager

  /**
   * Whether the Firebase SDK is currently running.
   *
   * @see startFirebaseSdk
   * @see stopFirebaseSdkIfRunning
   */
  @VisibleForTesting
  inline val isFirebaseSdkRunning
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
  constructor() : this(requester = Injector.get(), WebPush()) {
    setCoroutineContext(Dispatchers.IO)
  }

  override fun onCreate() {
    super.onCreate()
    startFirebaseSdkIfNotRunning()
    lifecycleState = Lifecycle.State.CREATED
  }

  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    sendLastPushNotification()
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    subscribe(token)
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
   * @param actorID ID of the authenticated [Actor] by which the form data is being appended.
   * @param token Key that identifies each Firebase Cloud Messaging (FCM) client instance.
   * @see subscribe
   */
  @VisibleForTesting
  fun appendSubscriptionFormData(builder: StringValuesBuilder, actorID: String, token: String) {
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
      append(
        "subscription[endpoint]",
        URIBuilder.url()
          .scheme("https")
          .host("app.joinmastodon.org")
          .path("relay-to")
          .path("fcm")
          .path(token)
          .path(actorID)
          .build()
          .toString()
      )
      append("subscription[keys][auth]", webPush.base64EncodedClientAuthenticationKey)
      append("subscription[keys][p256dh]", webPush.base64EncodedClientPublicKey)
    }
  }

  /**
   * Suspends until the specified system notification gets sent.
   *
   * @param id ID of the [Notification] whose delivery will be synchronously awaited.
   * @see StatusBarNotification.getId
   */
  @VisibleForTesting
  suspend fun awaitUntilSent(id: Int) = suspendCoroutine {
    @Suppress("ControlFlowWithEmptyBody") while (hasNotBeenSent(id)) {}
    it.resume(Unit)
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
      .let { Firebase.initialize(this, /* options = */ it) }
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
   * the AES/GCM-decrypting and the extended-Z85-decoding phases, which would require Orca to test
   * both of them and, thus, compute encrypted and encoded inputs and test encryption and encoding
   * themselves.
   *
   * While not ideal, performing a request to fetch the last notification seems to be the plausible
   * solution for now.
   *
   * @see onMessageReceived
   */
  private fun sendLastPushNotification() {
    coroutineScope.launch {
      val pushNotification = getLastPushNotification()
      val channel = pushNotification.type.toNotificationChannel(this@PushNotificationService)
      val id = pushNotification.generateSystemNotificationID()
      val notification =
        pushNotification.toNotification(this@PushNotificationService, authenticationLock)
      notificationManager.createNotificationChannel(channel)
      notificationManager.notify(id, notification)
      sentSystemPushNotificationIds += id
    }
  }

  /** Obtains the DTO of the notification received lastly from the Mastodon server. */
  private suspend fun getLastPushNotification() =
    requester
      .authenticated()
      .get(HostedURLBuilder::buildNotificationsRoute)
      .bodyAsText()
      .let { Json.decodeFromString(dtosSerializer, it) }
      .last()

  /**
   * Verifies whether the specified [Notification] has _not_ been sent.
   *
   * @param id ID of the [Notification] whose current absence of delivery will be checked.
   */
  private fun hasNotBeenSent(id: Int) =
    notificationManager.activeNotifications?.map(StatusBarNotification::getId)?.contains(id)?.not()
      ?: true

  /**
   * Subscribes to the receipt of updates from the Mastodon server.
   *
   * @param token Key that identifies each Firebase Cloud Messaging (FCM) client instance.
   */
  private fun subscribe(token: String) {
    coroutineScope.launch {
      authenticationLock.scheduleUnlock {
        requester.authenticated().post(
          HostedURLBuilder::buildNotificationSubscriptionPushingRoute
        ) {
          parameters { appendSubscriptionFormData(this, actorID = it.id, token) }
        }
      }
    }
  }

  /**
   * Cancels all system push notifications that have been sent by this [PushNotificationService].
   */
  private fun cancelSentNotifications() {
    for (sentNotificationID in sentSystemPushNotificationIds) {
      notificationManager.cancel(sentNotificationID)
    }
    sentSystemPushNotificationIds.clear()
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
    /** [KSerializer] that serializes the response to a request for obtaining notifications. */
    @JvmStatic @VisibleForTesting val dtosSerializer = ListSerializer(PushNotification.serializer())
  }
}

/**
 * Builds a [URI] to which `POST` HTTP requests intended to push a subscription for receiving
 * [PushNotification]s are sent.
 */
@InternalNotificationApi
@VisibleForTesting
internal fun HostedURLBuilder.buildNotificationSubscriptionPushingRoute() =
  path("api").path("v1").path("push").path("subscription").build()

/** Builds a [URI] to which `POST` HTTP requests for fetching notifications are sent. */
@InternalNotificationApi
@VisibleForTesting
internal fun HostedURLBuilder.buildNotificationsRoute() =
  path("api").path("v1").path("notifications").build()

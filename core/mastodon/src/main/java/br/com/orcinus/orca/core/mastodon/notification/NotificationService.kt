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
import android.service.notification.StatusBarNotification
import androidx.annotation.VisibleForTesting
import androidx.core.content.getSystemService
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URI
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.ECPublicKey
import java.util.Base64
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException

/**
 * [FirebaseMessagingService] that subscribes to the receipt of updates regarding the authenticated
 * [Actor] from the Mastodon server and forwards them to their device via system notifications. Each
 * notification type defined by the Mastodon documentation as of v1 ("favourite", "follow", "follow
 * request", "mention", "poll", "reblog", "severed relationships", "status" and "update") will have
 * a channel of its own when their notifications are sent.
 *
 * @param coroutineContext [CoroutineContext] in which payload conversions and requests are
 *   performed.
 * @property requester [Requester] by which push subscriptions are performed.
 * @property authenticationLock [AuthenticationLock] for requiring an authenticated [Actor] when
 *   converting received payloads into system notifications.
 * @see MastodonNotification.Type
 * @see MastodonNotification.Type.toNotificationChannel
 */
internal class NotificationService(
  private val requester: Requester,
  private val authenticationLock: SomeAuthenticationLock,
  coroutineContext: CoroutineContext
) : FirebaseMessagingService() {
  /**
   * IDs of [Notification]s that have been sent and have not yet been cleared by this
   * [NotificationService].
   *
   * @see StatusBarNotification.getId
   */
  private val activeNotificationIDs = mutableSetOf<Int>()

  /** Private randomly generated key that identifies this [NotificationService]. */
  private val authKey by lazy { ByteArray(16).apply(SecureRandom()::nextBytes).decodeToString() }

  /**
   * Base64-encoded [String] consisting of three sequences of bytes: an
   * [UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER] and the x and y affine coordinates of the generated
   * elliptic curve key.
   */
  private val publicKey by lazy {
    KeyPairGenerator.getInstance("EC")
      .apply { initialize(/* keysize = */ 256) }
      .generateKeyPair()
      .let { it.public as ECPublicKey }
      .let { it.w.affineX.toByteArray() to it.w.affineY.toByteArray() }
      .let { (x, y) -> byteArrayOf(UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER, *x, *y) }
      .let(Base64.getUrlEncoder().withoutPadding()::encodeToString)
  }

  /**
   * [CoroutineScope] in which remotely received payloads are converted into [MastodonNotification]
   * DTOs and requests to subscribe to the receipt of notifications from the Mastodon server are
   * sent.
   *
   * @see sendNotificationIfNotEmpty
   * @see pushSubscription
   */
  @VisibleForTesting val coroutineScope = CoroutineScope(coroutineContext)

  /**
   * [NotificationManager] through which updates received from the server will be forwarded in the
   * form of system notifications.
   */
  @VisibleForTesting
  val notificationManager
    get() = getSystemService<NotificationManager>() as NotificationManager

  @Throws(Module.DependencyNotInjectedException::class)
  constructor() :
    this(
      requester = Injector.get(),
      authenticationLock = Injector.get(),
      coroutineContext = SupervisorJob() + Dispatchers.IO
    )

  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    sendNotificationIfNotEmpty(message.data)
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    pushSubscription(token)
  }

  override fun onDestroy() {
    super.onDestroy()
    cancelSentNotifications()
    coroutineScope.cancel("Service has been destroyed.")
  }

  /**
   * Sends a system notification in case the given [payload] is not empty. In case it is, indeed,
   * not empty, but contains unknown values or does not have the ones required by a
   * [MastodonNotification], a [SerializationException] will be thrown.
   *
   * @param payload JSON [Map] structured as a notification DTO.
   * @throws SerializationException If the [payload] is missing required values or has unknown ones.
   * @see sendNotification
   */
  @Throws(SerializationException::class)
  private fun sendNotificationIfNotEmpty(payload: Map<String, String>) {
    if (payload.isNotEmpty()) {
      sendNotification(payload)
    }
  }

  /**
   * Sends a system notification based on the given [payload], disregarding whether it is empty or
   * not. As converting a [MastodonNotification] into a system notification is a suspending
   * operation, it is sent in the [coroutineScope]; thus, this method will probably return _before_
   * the notification is delivered to the device (this is specially important to keep in mind for
   * testing scenarios).
   *
   * @param payload (Non-empty) JSON [Map] structured as a notification DTO.
   * @throws SerializationException If the [payload] is missing required values or has unknown ones.
   */
  @Throws(SerializationException::class)
  private fun sendNotification(payload: Map<String, String>) {
    val dto = MastodonNotification.from(payload)
    val channel = dto.type.toNotificationChannel(this)
    val id = dto.normalizedID
    coroutineScope.launch {
      val notification = dto.toNotification(this@NotificationService, authenticationLock)
      notificationManager.createNotificationChannel(channel)
      notificationManager.notify(id, notification)
      activeNotificationIDs += id
    }
  }

  /**
   * Subscribes to the receipt of updates from the Mastodon server.
   *
   * @param token Key that identifies each Firebase Cloud Messaging (FCM) client instance.
   */
  private fun pushSubscription(token: String) {
    coroutineScope.launch {
      requester.authenticated().post(
        route = HostedURLBuilder::buildForNotificationSubscriptionPushing
      ) {
        parameters {
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
          append("subscription[keys][auth]", authKey)
          append("subscription[keys][p256dh]", publicKey)
        }
      }
    }
  }

  private fun cancelSentNotifications() {
    activeNotificationIDs.onEach(notificationManager::cancel).clear()
  }

  companion object {
    /**
     * Standardized by "SEC 1: Elliptic Curve Cryptography", it is the leading byte of an elliptic
     * curve key which denotes that it is uncompressed — that is, the two bytes that follow are both
     * its x and y affine coordinates.
     */
    private const val UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER: Byte = 0x04
  }
}

/**
 * Builds a [URI] to which `POST` HTTP requests intended to push a subscription for receiving
 * [MastodonNotification]s is sent.
 */
@VisibleForTesting
fun HostedURLBuilder.buildForNotificationSubscriptionPushing(): URI {
  return path("api").path("v1").path("push").path("subscription").build()
}

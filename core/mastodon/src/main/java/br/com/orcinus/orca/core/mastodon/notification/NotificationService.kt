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

import android.app.NotificationManager
import androidx.annotation.VisibleForTesting
import androidx.core.content.getSystemService
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
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

internal class NotificationService(
  private val requester: Requester,
  private val authenticationLock: SomeAuthenticationLock,
  coroutineContext: CoroutineContext
) : FirebaseMessagingService() {
  private val activeNotificationIDs = mutableSetOf<Int>()
  private val authKey by lazy { ByteArray(16).apply(SecureRandom()::nextBytes).decodeToString() }
  private val publicKey by lazy {
    KeyPairGenerator.getInstance("EC")
      .apply { initialize(/* keysize = */ 256) }
      .generateKeyPair()
      .let { it.public as ECPublicKey }
      .let { it.w.affineX.toByteArray() to it.w.affineY.toByteArray() }
      .let { (x, y) -> byteArrayOf(UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER, *x, *y) }
      .let(Base64.getUrlEncoder().withoutPadding()::encodeToString)
  }

  @VisibleForTesting val coroutineScope = CoroutineScope(coroutineContext)

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

  private fun sendNotificationIfNotEmpty(payload: Map<String, String>) {
    if (payload.isNotEmpty()) {
      sendNotification(payload)
    }
  }

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

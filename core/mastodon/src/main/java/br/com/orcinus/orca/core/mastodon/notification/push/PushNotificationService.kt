/*
 * Copyright © 2024–2025 Orcinus
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

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import androidx.annotation.VisibleForTesting
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.push.web.WebPushClient
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import io.ktor.util.StringValuesBuilder
import java.net.URI
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

/**
 * Subscribes to the receipt of updates regarding the authenticated [Actor] from the Mastodon server
 * and forwards them to their device via system notifications. Each notification type defined by the
 * Mastodon documentation as of v1 ("favourite", "follow", "follow request", "mention", "poll",
 * "reblog", "severed relationships", "status" and "update") will have a channel of its own when
 * their notifications are sent.
 *
 * @property requester [Requester] by which push subscriptions are performed.
 * @property webPushClient [WebPushClient] by which the keys for encryption and decryption of
 *   received updates are provided.
 * @see PushNotification.Type
 * @see PushNotification.Type.toNotificationChannel
 */
@InternalNotificationApi
internal class PushNotificationService
@VisibleForTesting
constructor(private val requester: Requester<*>, private val webPushClient: WebPushClient) :
  Service() {
  /** [Binder] for interprocess communication (IPC). */
  private var binder: Binder? = null

  /**
   * System notification IDs of push notifications that have been sent and have not yet been cleared
   * by this [PushNotificationService].
   *
   * @see PushNotification.generateSystemNotificationID
   */
  private val sentSystemPushNotificationIds = mutableSetOf<Int>()

  /**
   * [CoroutineScope] in which remotely received payloads are converted into [PushNotification] DTOs
   * and requests to subscribe to the receipt of notifications from the Mastodon server are sent.
   *
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

  @Throws(
    Injector.ModuleNotRegisteredException::class,
    Module.DependencyNotInjectedException::class
  )
  constructor() : this(requester = Injector.get(), WebPushClient()) {
    setCoroutineContext(Dispatchers.IO)
  }

  override fun onCreate() {
    super.onCreate()
    lifecycleState = Lifecycle.State.CREATED
    binder = Binder()
  }

  override fun onBind(intent: Intent?) = binder

  override fun onDestroy() {
    super.onDestroy()
    cancelSentNotifications()
    coroutineScope.cancel("Service has been destroyed.")
    binder = null
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
      append("subscription[endpoint]", "")
      append("subscription[keys][auth]", webPushClient.base64EncodedClientAuthenticationKey)
      append("subscription[keys][p256dh]", webPushClient.base64EncodedClientPublicKey)
    }
  }

  /** Subscribes to the receipt of updates from the Mastodon server. */
  private fun subscribe() {
    coroutineScope.launch {
      requester.authenticated().post(HostedURLBuilder::buildNotificationSubscriptionPushingRoute) {
        parameters { appendSubscriptionFormData(this) }
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

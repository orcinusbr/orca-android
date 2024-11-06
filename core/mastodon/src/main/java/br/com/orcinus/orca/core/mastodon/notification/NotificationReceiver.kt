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

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationService
import br.com.orcinus.orca.core.mastodon.notification.service.OnMessageReceiptListener
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import org.unifiedpush.android.connector.FailedReason
import org.unifiedpush.android.connector.MessagingReceiver
import org.unifiedpush.android.connector.data.PushEndpoint
import org.unifiedpush.android.connector.data.PushMessage

/**
 * Starts a [NotificationService], by which updates received from the Mastodon server are sent as
 * system notifications, and notifies the on-message-receipt listener injected by such service
 * whenever an update is received.
 *
 * @see OnMessageReceiptListener
 * @see Injector.inject
 */
internal class NotificationReceiver @InternalNotificationApi @VisibleForTesting constructor() :
  MessagingReceiver() {
  /**
   * [Intent]s with which unstopped services to which a connection has been bound have been created
   * by this receiver. Useful for when the endpoint is updated, allowing for them to be stopped and
   * preventing forwarding the notifications to outdated endpoints.
   *
   * @see stopBoundServices
   */
  private val boundServicesIntents = mutableListOf<Intent>()

  override fun onRegistrationFailed(context: Context, reason: FailedReason, instance: String) {
    stopBoundServices(context)
  }

  override fun onNewEndpoint(context: Context, endpoint: PushEndpoint, instance: String) {
    val intent = NotificationService.createIntent(context, endpoint.url)
    NotificationService.bind(context, intent)
    boundServicesIntents += intent
  }

  @Throws(Module.DependencyNotInjectedException::class)
  override fun onMessage(context: Context, message: PushMessage, instance: String) {
    Injector.get<OnMessageReceiptListener>()()
  }

  override fun onUnregistered(context: Context, instance: String) {
    stopBoundServices(context)
  }

  /**
   * Stops the services to which a connection is currently bound.
   *
   * @param context [Context] in which the services have been created.
   */
  private fun stopBoundServices(context: Context) {
    for (boundServiceIntent in boundServicesIntents) {
      context.stopService(boundServiceIntent)
    }
    boundServicesIntents.clear()
  }

  companion object {
    /**
     * [IntentFilter] containing the UnifiedPush-specific actions with which a receiver is
     * registered.
     *
     * @see register
     */
    @JvmStatic
    private val filter =
      IntentFilter().apply {
        addAction("org.unifiedpush.android.connector.MESSAGE")
        addAction("org.unifiedpush.android.connector.NEW_ENDPOINT")
        addAction("org.unifiedpush.android.connector.REGISTRATION_FAILED")
        addAction("org.unifiedpush.android.connector.UNREGISTERED")
      }

    /**
     * Registers a [NotificationReceiver].
     *
     * @param context [Context] in which the receiver will be registered.
     */
    @JvmStatic
    fun register(context: Context) {
      val receiver = NotificationReceiver()
      register(context, receiver)
    }

    /**
     * Registers the [receiver].
     *
     * @param context [Context] in which the [receiver] will be registered.
     * @param receiver [NotificationReceiver] to be registered.
     */
    @InternalNotificationApi
    @JvmStatic
    @VisibleForTesting
    fun register(context: Context, receiver: NotificationReceiver) {
      ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }
  }
}

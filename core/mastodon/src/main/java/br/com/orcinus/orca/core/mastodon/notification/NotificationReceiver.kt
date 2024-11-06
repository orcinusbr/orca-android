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
import android.content.ServiceConnection
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationService
import br.com.orcinus.orca.core.mastodon.notification.service.OnMessageReceiptListener
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import org.unifiedpush.android.connector.FailedReason
import org.unifiedpush.android.connector.MessagingReceiver
import org.unifiedpush.android.connector.UnifiedPush
import org.unifiedpush.android.connector.data.PushEndpoint
import org.unifiedpush.android.connector.data.PushMessage

/**
 * Starts a [NotificationService], by which updates received from the Mastodon server are sent as
 * system notifications, and notifies the on-message-receipt listener injected by such service
 * whenever an update is received.
 *
 * @property context [Context] in which this receiver is registered.
 * @property actor Authenticated [Actor] for configuring UnifiedPush.
 * @see OnMessageReceiptListener
 * @see Injector.inject
 */
internal class NotificationReceiver @InternalNotificationApi @VisibleForTesting constructor() :
  MessagingReceiver() {
  /**
   * [Intent]s with which services have been bound by this receiver, paired to their connections.
   * Useful for when the endpoint is updated, allowing for them to be killed and preventing
   * forwarding the notifications to outdated endpoints.
   *
   * @see killBoundServices
   */
  private val bindings = mutableListOf<Pair<Intent, ServiceConnection>>()

  override fun onRegistrationFailed(context: Context, reason: FailedReason, instance: String) {
    killBoundServices(context)
  }

  override fun onNewEndpoint(context: Context, endpoint: PushEndpoint, instance: String) {
    val intent = NotificationService.createIntent(context, endpoint.url)
    bindings += intent to NotificationService.bind(context, intent)
  }

  @Throws(Module.DependencyNotInjectedException::class)
  override fun onMessage(context: Context, message: PushMessage, instance: String) {
    Injector.get<OnMessageReceiptListener>()()
  }

  override fun onUnregistered(context: Context, instance: String) {
    killBoundServices(context)
  }

  /**
   * Unbinds and stops bound services.
   *
   * @param context [Context] in which the services have been bound.
   */
  private fun killBoundServices(context: Context) {
    for ((intent, connection) in bindings) {
      context.unbindService(connection)
      context.stopService(intent)
    }
    bindings.clear()
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
     * @property actor Authenticated [Actor] for configuring UnifiedPush.
     */
    @JvmStatic
    fun register(context: Context, actor: Actor.Authenticated) =
      register(context, NotificationReceiver(), actor)

    /**
     * Registers the [receiver].
     *
     * @param context [Context] in which the [receiver] will be registered.
     * @param receiver [NotificationReceiver] to be registered.
     * @param actor Authenticated [Actor] for configuring UnifiedPush.
     */
    @InternalNotificationApi
    @JvmStatic
    @VisibleForTesting
    fun register(context: Context, receiver: NotificationReceiver, actor: Actor.Authenticated) {
      setUpOrRegisterUnifiedPushApp(context, actor)
      ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    /**
     * Configures UnifiedPush in case it has not been set up yet; otherwise, re-registers the app
     * (the [documentation](https://unifiedpush.org/developers/android/#register-for-push) leaves
     * the reason for doing so unexplained, stating only that "something may have broke". What is
     * "something" and when, why and how would it malfunction? Unclear).
     *
     * @param context [Context] in which the SDK is set up and the app is registered.
     * @param actor Authenticated [Actor] whose access token will be defined as the instance.
     * @see registerUnifiedPushApp
     * @see Actor.Authenticated.accessToken
     */
    private fun setUpOrRegisterUnifiedPushApp(context: Context, actor: Actor.Authenticated) =
      if (isUnifiedPushNotSetUp(context)) {
        setUpUnifiedPush(context, actor)
      } else {
        registerUnifiedPushApp(context, actor)
      }

    /**
     * Returns whether UnifiedPush has _not_ already been set up.
     *
     * @param context [Context] from which the currently acknowledged distributor will be obtained,
     *   since its absence is what determines that the SDK is not configured.
     */
    private fun isUnifiedPushNotSetUp(context: Context) =
      UnifiedPush.getAckDistributor(context) == null

    /**
     * Configures UnifiedPush by registering the app and defining the distributor.
     *
     * @param context [Context] from which the distributors are obtained and the app is registered.
     * @param actor Authenticated [Actor] whose access token will be defined as the instance.
     * @see registerUnifiedPushApp
     * @see Actor.Authenticated.accessToken
     */
    private fun setUpUnifiedPush(context: Context, actor: Actor.Authenticated) {
      val distributor = UnifiedPush.getDistributors(context).lastOrNull() ?: return
      UnifiedPush.saveDistributor(context, distributor)
      registerUnifiedPushApp(context, actor)
    }

    /**
     * Registers a UnifiedPush app.
     *
     * @param context [Context] in which the registration takes place.
     * @param actor Authenticated [Actor] whose access token will be defined as the instance.
     * @see Actor.Authenticated.accessToken
     */
    private fun registerUnifiedPushApp(context: Context, actor: Actor.Authenticated) =
      UnifiedPush.registerApp(
        context,
        instance = actor.accessToken,
        vapid = BuildConfig.mastodonfirebaseVapid
      )
  }
}

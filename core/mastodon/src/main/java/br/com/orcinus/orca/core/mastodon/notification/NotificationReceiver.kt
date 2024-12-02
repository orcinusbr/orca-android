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

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import java.lang.ref.WeakReference

/**
 * Receives broadcasts pertaining push notifications to be sent to the user.
 *
 * Note that registering this by directly calling [Context.registerReceiver] will most likely result
 * in an [IllegalStateException] being thrown when a broadcast is received, given that additional
 * configuration is required for its registration. [Companion.register] should called instead.
 *
 * Based on the
 * [`PushNotificationReceiver`](https://github.com/mastodon/mastodon-android/blob/9ed95cc0d34bcc7703a1948a2244eaf372221d87/mastodon/src/main/java/org/joinmastodon/android/PushNotificationReceiver.java)
 * (PNR) of the official Mastodon Android app.
 *
 * @property contextRef Reference to the context in which registration is performed.
 */
@InternalNotificationApi
internal class NotificationReceiver
private constructor(private val contextRef: WeakReference<Context>) :
  BroadcastReceiver(), AutoCloseable {
  /**
   * Amount of services to which a connection has been and is currently bound by this receiver. Such
   * binding is performed upon the receipt of a push intent is not undone unless [close] is
   * explicitly called.
   *
   * @see onReceive
   */
  private var boundServicesCount = 0

  /** Flags with which a connection is bound to a service by this receiver. */
  private val serviceBindingFlags =
    Context.BIND_AUTO_CREATE.`if`(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      or(@Suppress("InlinedApi") Context.BIND_NOT_PERCEPTIBLE)
    }

  /**
   * Intent with which a connection to a service is bound by this receiver when it is unlocked.
   * `null` in case the [context] has been garbage-collected.
   *
   * @see contextRef
   */
  private inline val serviceIntent
    get() = Intent(context, NotificationService::class.java)

  /** Context referenced by the [contextRef]; `null` if garbage-collected. */
  private inline val context
    get() = contextRef.get()

  /** Connection bound to a service, which does nothing upon connection and disconnection. */
  private object NoOpServiceConnection : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) = Unit

    override fun onServiceDisconnected(name: ComponentName?) = Unit
  }

  constructor(context: Context?) : this(WeakReference(context))

  @Throws(IllegalStateException::class)
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context != null && intent != null && filter.hasAction(intent.action)) {
      check(context == this.context) { "Instantiation and receipt contexts differ." }
      check(filter.hasAction(intent.action)) { "Received an unknown broadcast: $intent." }
      context.bindService(serviceIntent, NoOpServiceConnection, serviceBindingFlags)
      boundServicesCount++
    }
  }

  /**
   * Unbinds the connection bound to services when a push intent was first received and stops them.
   * In general, this method should _not_ be called from production code, as it interrupts the
   * listening to updates from the Mastodon server and, thus, their delivery to the user as push
   * notifications.
   */
  @VisibleForTesting
  override fun close() {
    context?.let {
      // Context.unbindService(ServiceConnection) throws if the service is unbound.
      try {
        it.unbindService(NoOpServiceConnection)
      } catch (_: IllegalArgumentException) {}

      repeat(boundServicesCount) { context?.stopService(serviceIntent) }
      boundServicesCount = 0
    }
  }

  companion object {
    /**
     * Action of intents received by a receiver.
     *
     * @see Intent.getAction
     */
    @InternalNotificationApi
    @VisibleForTesting
    const val ACTION = "com.google.android.c2dm.intent.RECEIVE"

    /**
     * Filter that specifies the action to be listened to by a receiver.
     *
     * @see ACTION
     */
    @JvmStatic private val filter = IntentFilter(ACTION)

    /**
     * Registers the [receiver].
     *
     * @param context Context in which the registration will be performed.
     * @param receiver Receiver to be registered.
     */
    @InternalNotificationApi
    @JvmStatic
    fun register(context: Context, receiver: NotificationReceiver) {
      ContextCompat.registerReceiver(
        context,
        receiver,
        filter,
        /* broadcastPermission = */ "com.google.android.c2dm.permission.SEND",
        /* scheduler = */ null,
        @Suppress("WrongConstant") ContextCompat.RECEIVER_EXPORTED
      )
    }
  }
}

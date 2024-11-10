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

package br.com.orcinus.orca.core.mastodon.test.notification

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.NotificationReceiver
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.starter.lifecycle.CompleteLifecycleActivity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [NotificationReceiverTestScope] implementation used by [runNotificationReceiverTest].
 *
 * @property context [Context] in which the [receiver] is registered.
 */
private class NotificationReceiverEnvironment(override val context: Context) :
  NotificationReceiverTestScope() {
  override fun register() = NotificationReceiver.register(context, receiver, actor)

  /**
   * Unregisters the [receiver] in case it has been and is currently registered.
   *
   * @see register
   */
  fun unregister() =
    try {
      context.unregisterReceiver(receiver)
    } catch (_: IllegalArgumentException) {}
}

/** [Activity] in which a [NotificationReceiver] test is run. */
@InternalNotificationApi
class NotificationReceiverActivity : CompleteLifecycleActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(br.com.orcinus.orca.platform.autos.R.style.Theme_Autos)
    super.onCreate(savedInstanceState)
  }
}

/** Scope in which a [NotificationReceiver] test is run. */
sealed class NotificationReceiverTestScope {
  /** [Context] in which this [NotificationReceiverTestScope] is. */
  abstract val context: Context

  /**
   * Authenticated [Actor] with which the [receiver] is registered.
   *
   * @see register
   */
  val actor = Actor.Authenticated.sample

  /** [NotificationReceiver] being tested. */
  val receiver = @Suppress("DiscouragedApi") NotificationReceiver()

  /**
   * Registers the [receiver].
   *
   * @see NotificationReceiver.register
   */
  abstract fun register()
}

/**
 * Runs a [NotificationReceiver] test in an [Activity].
 *
 * @param body Testing to be performed.
 */
@OptIn(ExperimentalContracts::class)
fun runNotificationReceiverTest(body: NotificationReceiverTestScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  launchActivity<NotificationReceiverActivity>().use { scenario ->
    scenario.onActivity { activity: NotificationReceiverActivity ->
      runNotificationReceiverTest(activity, body)
    }
  }
}

/**
 * Runs a [NotificationReceiver] test.
 *
 * @param context [Context] in which the [body] is executed.
 * @param body Testing to be performed.
 */
@OptIn(ExperimentalContracts::class)
fun runNotificationReceiverTest(context: Context, body: NotificationReceiverTestScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val scope = NotificationReceiverEnvironment(context)
  try {
    scope.body()
  } finally {
    scope.unregister()
  }
}

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
import kotlin.time.Duration

/**
 * Class responsible for building test-focused [NotificationLock]s.
 *
 * @see build
 */
internal class NotificationLockBuilder {
  /** Lambda to be invoked whenever the built [NotificationLock]'s elapsed time is obtained. */
  private var getElapsedTime = { Duration.ZERO }

  /** Lambda to be invoked whenever permission to send notifications is requested. */
  private var requestPermission = {}

  /** Lambda to be invoked whenever an unlock is performed on the built [NotificationLock]. */
  private var onUnlock = {}

  /**
   * [NotificationLock] built by this [NotificationLockBuilder].
   *
   * @param context [Context] in which the request is performed.
   */
  private inner class BuiltNotificationLock(context: Context) : NotificationLock(context) {
    override fun getElapsedTime() = this@NotificationLockBuilder.getElapsedTime()

    override fun requestPermission() = this@NotificationLockBuilder.requestPermission()

    override fun onUnlock() = this@NotificationLockBuilder.onUnlock()
  }

  /**
   * Defines the elapsed time to be provided.
   *
   * @param getElapsedTime Lambda invoked whenever its analogous method is called.
   * @see NotificationLock.getElapsedTime
   */
  fun getElapsedTime(getElapsedTime: () -> Duration) = apply {
    this.getElapsedTime = getElapsedTime
  }

  /**
   * Defines permission-requesting behavior.
   *
   * @param requestPermission Lambda invoked whenever its analogous method is called.
   * @see NotificationLock.requestPermission
   */
  fun requestPermission(requestPermission: () -> Unit) = apply {
    this.requestPermission = requestPermission
  }

  /**
   * Defines the action to be performed when an unlock is performed.
   *
   * @param onUnlock Lambda invoked whenever its analogous method is called.
   * @see NotificationLock.onUnlock
   */
  fun onUnlock(onUnlock: () -> Unit) = apply { this.onUnlock = onUnlock }

  /**
   * Builds a [NotificationLock] with the specified configuration.
   *
   * @param context [Context] in which the request is performed.
   */
  fun build(context: Context): NotificationLock = BuiltNotificationLock(context)
}

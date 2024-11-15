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

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import java.lang.ref.WeakReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

/**
 * [NotificationLock] whose elapsed time is provided by the system and that launches the [launcher]
 * with the notification permission upon an unlock. Implementation constructed by the public factory
 * method.
 *
 * @param context [Context] in which the request is performed.
 * @param launcher [ActivityResultLauncher] by which permission to send notifications is requested
 *   to be granted by the user. Because asking for this specific one requires an API level of >= 33,
 *   when [requestUnlock] is called, it will be launched only in case such condition is met.
 * @see ActivityResultLauncher.launch
 * @see Manifest.permission.POST_NOTIFICATIONS
 * @see requestUnlock
 */
private class DefaultNotificationLock(
  context: Context,
  private val launcher: ActivityResultLauncher<String>
) : NotificationLock(context) {
  override fun getElapsedTime() = System.currentTimeMillis().milliseconds

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun requestPermission() = launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
}

/**
 * Handles performance of timed-spaced permission requests for sending notifications in API levels
 * in which such request is supported or directly binding the [Service] by which updates are
 * listened to from the Mastodon server and forwarded to the device as system notifications; this
 * distinction is necessary because [Manifest.permission.POST_NOTIFICATIONS] is not available in OS
 * versions older than 33 (Tiramisu).
 *
 * @property contextRef [WeakReference] to the [Context] in which the request is performed.
 * @see permissionRequestInterval
 * @see Build.VERSION_CODES.TIRAMISU
 * @see ComponentActivity.registerForActivityResult
 * @see ActivityResultLauncher.launch
 */
abstract class NotificationLock
private constructor(private val contextRef: WeakReference<Context>) {
  /**
   * [SharedPreferences] for storing the Unix moment in time at which the request was last
   * performed. Since notifications are not an integral, yet important functionality of the default
   * version of Orca whose absence may be confusing to the user (even if they have chosen to not
   * grant permission before), an interval of sixteen days between each request is arbitrarily set.
   *
   * @see timeSinceLastPermissionRequest
   * @see permissionRequestInterval
   */
  private val preferences by lazy {
    context?.getSharedPreferences(this::class.qualifiedName, Context.MODE_PRIVATE)
  }

  /** [Context] referenced by the [contextRef]; `null` if garbage-collected. */
  private inline val context
    get() = contextRef.get()

  /**
   * Interval between now and the last time a request for the user to grant permission to send
   * notifications was performed. Infinite when either the [preferences] are unavailable (due to the
   * [context] having been garbage-collected) or such a request has never been made.
   *
   * @see Duration.INFINITE
   */
  private inline val timeSinceLastPermissionRequest: Duration
    get() {
      val elapsedTime = getElapsedTime()
      val lastPermissionRequestTime = lastPermissionRequestTime
      return if (elapsedTime.isFinite() && lastPermissionRequestTime != null) {
        elapsedTime - lastPermissionRequestTime
      } else {
        Duration.INFINITE
      }
    }

  /**
   * Moment at which permission for sending notifications was last requested. `null` when the
   * [preferences] are unavailable (because the [context] has been garbage-collected) or such a
   * request has never been performed.
   */
  private inline var lastPermissionRequestTime
    get() =
      preferences
        ?.getLong(LAST_PERMISSION_REQUEST_TIME_PREFERENCE_KEY, NEVER)
        ?.takeUnless(NEVER::equals)
        ?.milliseconds
    set(lastPermissionRequestTime) {
      preferences?.edit {
        lastPermissionRequestTime?.let {
          putLong(LAST_PERMISSION_REQUEST_TIME_PREFERENCE_KEY, it.inWholeMilliseconds)
        }
          ?: remove(LAST_PERMISSION_REQUEST_TIME_PREFERENCE_KEY)
      }
    }

  /**
   * Whether permission to send notifications has _not_ been granted by the user — either because
   * they disallowed it earlier or it was never requested before.
   */
  private inline val isPermissionNotGranted
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    get() =
      context
        ?.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        ?.equals(PackageManager.PERMISSION_DENIED)
        ?: true

  @InternalNotificationApi internal constructor(context: Context?) : this(WeakReference(context))

  /**
   * Requests, within the [permissionRequestInterval], the user for permission to send notifications
   * and then invokes binds the notification [Service] in case it is granted. If the current API
   * level does not support requesting such permission, it is directly bound.
   */
  fun requestUnlock() {
    if (
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        isPermissionNotGranted &&
        (timeSinceLastPermissionRequest.isInfinite() ||
          timeSinceLastPermissionRequest >= permissionRequestInterval)
    ) {
      requestPermission()
      lastPermissionRequestTime = getElapsedTime()
    } else {
      lastPermissionRequestTime = null
      onUnlock(context)
    }
  }

  /** Obtains the amount of time that has elapsed until now. */
  protected abstract fun getElapsedTime(): Duration

  /** Requests to the user permission for sending notifications. */
  @RequiresApi(Build.VERSION_CODES.TIRAMISU) protected abstract fun requestPermission()

  @InternalNotificationApi
  @VisibleForTesting
  internal companion object {
    /**
     * Key that identifies the preference characterizing the Unix time at which permission to send
     * notifications was last requested to the user.
     */
    private const val LAST_PERMISSION_REQUEST_TIME_PREFERENCE_KEY = "last-permission-request"

    /**
     * Initial value of the last permission request time preference which denotes that such a
     * request has not been performed before.
     *
     * @see preferences
     * @see LAST_PERMISSION_REQUEST_TIME_PREFERENCE_KEY
     */
    private const val NEVER = -1L

    /** Interval in which permission to send notifications is requested to the user. */
    @JvmStatic val permissionRequestInterval = 16.days
  }
}

/**
 * Creates a [NotificationLock].
 *
 * This factory method _must_ be called before the [activity] is started, since it registers a
 * request to receive a result — that of asking for the permission — right before the class instance
 * gets created; not doing so will result in an [IllegalStateException] being thrown.
 *
 * @param activity [ComponentActivity] in which the request is performed.
 * @throws IllegalStateException If the [activity] has already been started.
 */
@Throws(IllegalStateException::class)
fun NotificationLock(activity: ComponentActivity): NotificationLock =
  DefaultNotificationLock(
    activity,
    activity.registerForActivityResult(RequestPermission()) { isGranted ->
      if (isGranted) {
        onUnlock(activity)
      }
    }
  )

/**
 * Callback called whenever either permission to send notifications is granted or the current API
 * level does not support such request. Essentially, just acts a proxy for calling
 * [NotificationService.bind] with the given [context] passed in as the parameter.
 *
 * @param context [Context] in which a connection to a [NotificationService] is bound — if
 *   non-`null`.
 */
private fun onUnlock(context: Context?) {
  context?.let(NotificationService::bind)
}

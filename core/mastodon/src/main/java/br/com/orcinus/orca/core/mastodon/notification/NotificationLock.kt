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
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import java.lang.ref.WeakReference

/**
 * Handles requesting permission for sending notifications in API levels in which such request is
 * supported or directly binding the [Service] by which updates are listened to from the Mastodon
 * server and forwarded to the device as system notifications; this distinction is necessary because
 * [Manifest.permission.POST_NOTIFICATIONS] is not available in OS versions older than 33
 * (Tiramisu).
 *
 * @property contextRef [WeakReference] to the [Context] in which the request is performed.
 * @property launcher [ActivityResultLauncher] by which permission to send notifications is
 *   requested to be granted by the user. Because asking for this specific one requires an API level
 *   of >= 33, when [unlock] is called, it will be launched only in case such condition is met.
 * @see Build.VERSION_CODES.TIRAMISU
 * @see ComponentActivity.registerForActivityResult
 * @see ActivityResultLauncher.launch
 */
class NotificationLock
private constructor(
  private val contextRef: WeakReference<Context>,
  private val launcher: ActivityResultLauncher<String>
) {
  /** [Context] referenced by the [contextRef]; `null` if garbage-collected. */
  private inline val context
    get() = contextRef.get()

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

  @InternalNotificationApi
  @VisibleForTesting
  internal constructor(
    context: Context,
    launcher: ActivityResultLauncher<String>
  ) : this(WeakReference(context), launcher)

  /**
   * Requests the user for permission to send notifications and then invokes the [onUnlock] listener
   * lambda in case it is granted. If the current API level does not support requesting such
   * permission, that lambda is invoked directly.
   */
  fun unlock() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isPermissionNotGranted) {
      launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    } else {
      onUnlock(context)
    }
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
fun NotificationLock(activity: ComponentActivity) =
  NotificationLock(
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

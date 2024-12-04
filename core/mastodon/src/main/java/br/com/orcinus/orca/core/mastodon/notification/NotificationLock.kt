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
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import br.com.orcinus.orca.core.mastodon.notification.push.PushNotificationService
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import java.lang.ref.WeakReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

/**
 * [NotificationLock] whose elapsed time is provided by the system and that launches the [launcher]
 * with the notification permission followed by a load of the native library upon an unlock.
 * Implementation constructed by the public factory method. This class _must_ be instantiated before
 * the given [ComponentActivity] is started, since it registers a request to receive a result at the
 * very moment it gets created.
 *
 * @param activity [ComponentActivity] in which the request is performed.
 * @see ActivityResultLauncher.launch
 * @see Manifest.permission.POST_NOTIFICATIONS
 * @see System.loadLibrary
 * @see requestUnlock
 */
private class DefaultNotificationLock(activity: ComponentActivity) : NotificationLock(activity) {
  /**
   * [ActivityResultLauncher] by which permission to send notifications is requested to be granted
   * by the user. Because asking for this specific one requires an API level of >= 33, when
   * [requestUnlock] is called, it will be launched only in case such condition is met.
   */
  private val launcher =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      activity.registerForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) {
          unlock()
        }
      }
    } else {
      null
    }

  override fun getElapsedTime() = System.currentTimeMillis().milliseconds

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun requestPermission() {
    launcher?.launch(Manifest.permission.POST_NOTIFICATIONS)
  }

  override fun onUnlock() = System.loadLibrary("core-mastodon")
}

/**
 * Handles performance of timed-spaced permission requests for sending notifications in API levels
 * in which such request is supported or directly registering the [BroadcastReceiver] by which
 * updates are listened to from the Mastodon server and forwarded to the device as system
 * notifications; this distinction is necessary because [Manifest.permission.POST_NOTIFICATIONS] is
 * not available in OS versions older than 33 (Tiramisu).
 *
 * @property contextRef [WeakReference] to the [Context] in which the request is performed.
 * @see permissionRequestIntervalThreshold
 * @see Build.VERSION_CODES.TIRAMISU
 * @see ComponentActivity.registerForActivityResult
 * @see ActivityResultLauncher.launch
 */
abstract class NotificationLock
private constructor(private val contextRef: WeakReference<Context>) : AutoCloseable {
  /**
   * [SharedPreferences] for storing the Unix moment in time at which the request was last
   * performed. Since notifications are not an integral, yet important functionality of the default
   * version of Orca whose absence may be confusing to the user (even if they have chosen to not
   * grant permission before), an interval of 16 days between each request is arbitrarily set.
   *
   * @see permissionRequestInterval
   * @see permissionRequestIntervalThreshold
   */
  private val preferences by lazy {
    context?.getSharedPreferences(this::class.qualifiedName, Context.MODE_PRIVATE)
  }

  /**
   * Amount of services to which a connection has been and is currently bound by this
   * [NotificationLock]. Such binding is performed upon the receipt of a push intent is not undone
   * unless [close] is explicitly called.
   */
  private var boundServicesCount = 0

  /** Flags with which a connection is bound to a service by this [NotificationLock]. */
  private val serviceBindingFlags =
    Context.BIND_AUTO_CREATE.`if`(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      or(@Suppress("InlinedApi") Context.BIND_NOT_PERCEPTIBLE)
    }

  /**
   * Intent with which a connection to a [PushNotificationService] is bound by this
   * [NotificationLock] when it is unlocked. `null` in case the [context] has been
   * garbage-collected.
   *
   * @see contextRef
   */
  private inline val serviceIntent
    get() = context?.let { Intent(it, PushNotificationService::class.java) }

  /** [Context] referenced by the [contextRef]; `null` if garbage-collected. */
  private inline val context
    get() = contextRef.get()

  /**
   * Time elapsed since the last request for the user to grant permission to send notifications was
   * performed. Infinite when either the [preferences] are unavailable (due to the [context] having
   * been garbage-collected) or such a request has never been made.
   *
   * @see Duration.INFINITE
   */
  private inline val permissionRequestInterval: Duration
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    get() {
      val elapsedTime = getElapsedTime()
      val permissionRequestTime = permissionRequestTime
      return if (elapsedTime.isFinite() && permissionRequestTime.isFinite()) {
        elapsedTime - permissionRequestTime
      } else {
        Duration.INFINITE
      }
    }

  /**
   * Moment at which permission for sending notifications was last requested. Infinite when the
   * [preferences] are unavailable (because the [context] has been garbage-collected) or such a
   * request has never been performed.
   *
   * @see Duration.INFINITE
   */
  private inline var permissionRequestTime
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    get() =
      preferences
        ?.getLong(PERMISSION_REQUEST_TIME_PREFERENCE_KEY, NEVER)
        ?.takeUnless(NEVER::equals)
        ?.milliseconds
        ?: Duration.INFINITE
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    set(permissionRequestTime) {
      preferences?.edit {
        putLong(PERMISSION_REQUEST_TIME_PREFERENCE_KEY, permissionRequestTime.inWholeMilliseconds)
      }
    }

  /**
   * Whether permission to send notifications has _not_ been granted by the user — either because
   * they disallowed it earlier or it was never requested before.
   */
  private inline val isPermissionNotGranted
    @ChecksSdkIntAtLeast(Build.VERSION_CODES.TIRAMISU)
    get() =
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        context
          ?.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
          ?.equals(PackageManager.PERMISSION_DENIED)
          ?: true

  /**
   * Connection bound to a [PushNotificationService], which does nothing upon connection and
   * disconnection.
   */
  @InternalNotificationApi
  @VisibleForTesting
  internal object NoOpServiceConnection : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) = Unit

    override fun onServiceDisconnected(name: ComponentName?) = Unit
  }

  @InternalNotificationApi
  @VisibleForTesting
  internal constructor(context: Context?) : this(WeakReference(context))

  /**
   * Requests an _unlock_ — the binding a connection to a [Service] which listens to updates
   * received from the Mastodon server and redirects them to the device as system notifications — by
   * asking the user to grant permission for sending notifications if the API level supports
   * [Manifest.permission.POST_NOTIFICATIONS].
   *
   * On API level >= 34, subsequent calls to this method are **not** guaranteed to prompt the user
   * for permission, as repeated requests could significantly diminish the quality of the user
   * experience in Orca. Therefore, the request will only be made if at least 16 days have passed
   * since the last request in case it was denied. After permission is granted, each future call to
   * this method will perform an unlock as long as the permission remains granted; if revoked, it is
   * requested again and the cycle repeats.
   *
   * On earlier API levels, however, that time constraint does not apply: both the first and
   * subsequent calls will perform an unlock.
   */
  fun requestUnlock() {
    if (isPermissionNotGranted) {
      if (
        permissionRequestInterval.isInfinite() ||
          permissionRequestInterval >= permissionRequestIntervalThreshold
      ) {
        requestPermission()
        permissionRequestTime = getElapsedTime()
      }
    } else {
      unlock()
    }
  }

  /**
   * Unbinds the connection bound to [PushNotificationService]s when an unlock was first performed
   * and stops them. In general, this method should _not_ be called from production code, as it
   * interrupts the listening to updates from the Mastodon server and, thus, their delivery to the
   * user as push notifications.
   */
  @VisibleForTesting
  override fun close() {
    val context = context ?: return
    val serviceIntent = serviceIntent ?: return
    val preferences = preferences ?: return

    // Context.unbindService(ServiceConnection) throws if the service is unbound.
    try {
      context.unbindService(NoOpServiceConnection)
    } catch (_: IllegalArgumentException) {}

    repeat(boundServicesCount) { _ -> context.stopService(serviceIntent) }
    boundServicesCount = 0
    preferences.edit(action = SharedPreferences.Editor::clear)
  }

  /** Obtains the amount of time that has elapsed until now. */
  protected abstract fun getElapsedTime(): Duration

  /**
   * Callback called whenever this [NotificationLock] is unlocked — that is, a request has been
   * performed and permission either is granted or has been granted before by the user to send
   * notifications.
   *
   * @see requestUnlock
   */
  protected abstract fun onUnlock()

  /** Requests to the user permission for sending notifications. */
  @RequiresApi(Build.VERSION_CODES.TIRAMISU) protected abstract fun requestPermission()

  /**
   * Called whenever either permission to send notifications is granted or the current API level
   * does not support such request. Essentially, binds a [PushNotificationService] and calls
   * [onUnlock] if the [context] has not been garbage-collected.
   */
  protected fun unlock() {
    val context = context ?: return
    val serviceIntent = serviceIntent ?: return
    context.bindService(serviceIntent, NoOpServiceConnection, serviceBindingFlags)
    boundServicesCount++
    onUnlock()
  }

  @InternalNotificationApi
  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  @VisibleForTesting
  internal companion object {
    /**
     * Key that identifies the preference characterizing the Unix time at which permission to send
     * notifications was last requested to the user.
     */
    private const val PERMISSION_REQUEST_TIME_PREFERENCE_KEY = "permission-request-time"

    /**
     * Initial value of the last permission request time preference which denotes that such a
     * request has not been performed before.
     *
     * @see preferences
     * @see PERMISSION_REQUEST_TIME_PREFERENCE_KEY
     */
    private const val NEVER = -1L

    /** Minimum time space between one notification permission request and another. */
    @JvmStatic val permissionRequestIntervalThreshold = 16.days
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
  DefaultNotificationLock(activity)

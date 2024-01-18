/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.starter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * Starts a [StartableActivity] with the configured settings.
 *
 * @param T [StartableActivity] to be started.
 * @param context [Context] from which the [StartableActivity] will be started.
 * @param activityClass [KClass] of the [StartableActivity].
 */
class ActivityStarter<T : StartableActivity>
@PublishedApi
internal constructor(private val context: Context, private val activityClass: KClass<T>) {
  /** Arguments to be passed to the [Intent]'s [extras][Intent.getExtras]. */
  private val args = hashMapOf<String, Any?>()

  /** [Intent]'s [flags][Intent.getFlags]. */
  private var flags = 0

  /**
   * Listens to when a [StartableActivity] has been started.
   *
   * @param A [StartableActivity] that's been started.
   * @see start
   */
  fun interface OnStartListener<A : StartableActivity> {
    fun onStart(activity: A)
  }

  /**
   * Defines the [Activity] as the first task of a new [Activity] group to be created. Useful for
   * when it isn't started from another [Activity].
   *
   * @see Intent.FLAG_ACTIVITY_NEW_TASK
   */
  fun asNewTask(): ActivityStarter<T> {
    return apply { flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK }
  }

  /**
   * Defines the arguments to be passed to the [Intent]'s [extras][Intent.getExtras].
   *
   * @param args Key-value [Pair]s representing the arguments for the [Activity].
   */
  fun with(vararg args: Pair<String, Any?>): ActivityStarter<T> {
    return apply { args.forEach { this.args[it.first] = it.second } }
  }

  /**
   * Starts the [StartableActivity].
   *
   * @param listener [OnStartListener] to be notified when the [StartableActivity] is started.
   * @see Context.startActivity
   */
  fun start(listener: OnStartListener<T>? = null) {
    val argsAsArray = args.map { (key, value) -> key to value }.toTypedArray()
    val extras = bundleOf(*argsAsArray)
    val intent = Intent(context, activityClass.java).putExtras(extras).addFlags(flags)
    listener?.let { listeners[activityClass] = it }
    context.startActivity(intent)
  }

  companion object {
    /**
     * [OnStartListener]s that have been registered associated to the [KClass] of the
     * [StartableActivity] to whose starting they listen.
     */
    private val listeners =
      mutableMapOf<KClass<out StartableActivity>, OnStartListener<out StartableActivity>>()

    /**
     * Notifies the [OnStartListener] that listens to the starting of the given [StartableActivity],
     * removing it afterwards.
     *
     * @param activity [StartableActivity] whose start is listened to by the [OnStartListener] to be
     *   notified.
     */
    internal fun notifyListenersOf(activity: StartableActivity) {
      listeners
        .filter { (activityClass, _) -> activityClass.isSuperclassOf(activity::class) }
        .values
        .filterIsInstance<OnStartListener<StartableActivity>>()
        .forEach { it.onStart(activity) }
        .also { listeners.remove(activity::class) }
    }
  }
}

/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.platform.navigation

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.orcinus.orca.platform.navigation.duplication.Duplication
import br.com.orcinus.orca.platform.navigation.duplication.allowingDuplication
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.transition.Transition

/**
 * Navigates to [Fragment]s through [navigate].
 *
 * @param fragmentManager [FragmentManager] that adds [Fragment]s to the [FragmentContainerView].
 * @param containerID ID of the [FragmentContainerView] to which [Fragment]s will be added.
 */
class Navigator
private constructor(
  private val fragmentManager: FragmentManager,
  @IdRes private val containerID: Int
) {
  /**
   * [Navigation.Destination.OnChangeListener]s that are currently listening to
   * [Navigation.Destination] changes.
   */
  private val onDestinationChangeListeners =
    mutableListOf<Navigation.Destination.OnChangeListener>()

  /**
   * Defines the [Destination.Provider] that will provide the [Fragment] destination through [to].
   */
  class Navigation<T : Fragment> internal constructor() {
    /**
     * Target navigation site.
     *
     * @param T [Fragment] to be navigated to.
     * @param route Path by which the [target] can be retrieved.
     * @param target Returns the [Fragment] of this [Destination].
     */
    data class Destination<T : Fragment>(val route: String, val target: () -> T) {
      /**
       * Provides the [Destination] to navigate to through [provide]. Can be instantiated via [to].
       *
       * @param T [Fragment] to navigate to.
       */
      abstract class Provider<T : Fragment> internal constructor() {
        /** Provides the [Destination] to navigate to. */
        internal abstract fun provide(): Destination<T>
      }

      /**
       * Listener to be notified whenever the [Destination] changes.
       *
       * @see onChange
       */
      fun interface OnChangeListener {
        /**
         * Callback that gets called when the [Destination] is defined as currently being the given
         * one.
         *
         * @param destination [Destination] that has been changed to.
         */
        fun onChange(destination: Destination<*>)
      }
    }

    /**
     * Creates a [Destination.Provider].
     *
     * @param route Path by which the result of [target] can be retrieved.
     * @param target Returns the [Fragment] to navigate to.
     */
    fun to(route: String, target: () -> T): Destination.Provider<T> {
      return object : Destination.Provider<T>() {
        override fun provide(): Destination<T> {
          return Destination(route, target)
        }
      }
    }
  }

  /**
   * Stores, in memory, [Navigator]s that have been created, allowing for them to be retrieved.
   * Ultimately, prevents re-instantiation of [Navigator]s.
   */
  internal object Pool {
    /**
     * [Navigator]s that have been created, associated to their respective [FragmentContainerView]s'
     * IDs, to be later retrieved.
     *
     * @see get
     */
    private val remembrances = hashMapOf<Int, Navigator>()

    /**
     * [LifecycleObserver] that removes the [Navigator] associated to the [containerID] after the
     * [FragmentActivity] is destroyed.
     *
     * @param containerID ID of the [FragmentContainerView] to which the [Navigator] to be removed
     *   has been associated when pooled.
     */
    private class RemovalLifecycleObserver(@IdRes private val containerID: Int) :
      DefaultLifecycleObserver {
      override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        (owner as FragmentActivity).lifecycle.removeObserver(this)
        remembrances.remove(containerID)
      }
    }

    /**
     * Creates a [Navigator] if no equivalent one for the [activity] has yet been instantiated,
     * storing it for later use, or retrieves the previously remembered one.
     *
     * @param activity [FragmentActivity] through which the [Navigator] will be obtained.
     * @throws IllegalStateException If no [FragmentContainerView] is found in the [activity].
     */
    @Throws(IllegalStateException::class)
    fun get(activity: FragmentActivity): Navigator {
      val containerID = getContainerIDOrThrow(activity)
      if (containerID !in this) {
        remember(activity, containerID)
      }
      return remembrances.getValue(containerID)
    }

    /**
     * Creates and stores a [Navigator], associating it to the given [containerID] and automatically
     * removing it from the [Pool] after the [activity] is destroyed.
     *
     * @param activity [FragmentActivity] after whose destruction the remembered [Navigator] will be
     *   removed.
     * @param containerID ID of the [FragmentContainerView] to which [Fragment]s will be added.
     */
    internal fun remember(activity: FragmentActivity, @IdRes containerID: Int) {
      val navigator = Navigator(activity.supportFragmentManager, containerID)
      val lifecycleObserver = RemovalLifecycleObserver(containerID)
      activity.runOnUiThread { activity.lifecycle.addObserver(lifecycleObserver) }
      remembrances[containerID] = navigator
    }

    /**
     * Obtains the ID of the [FragmentContainerView] (generating one for it if it hasn't already
     * been identified) contained by the [FragmentActivity].
     *
     * @param activity [FragmentActivity] in which the [FragmentContainerView] is.
     * @throws IllegalStateException If no [FragmentContainerView] is found.
     */
    @Throws(IllegalStateException::class)
    internal fun getContainerIDOrThrow(activity: FragmentActivity): Int {
      return activity
        .requireViewById<FrameLayout>(android.R.id.content)
        .get<FragmentContainerView>(isInclusive = false)
        .also(View::identify)
        .id
    }

    /**
     * Whether a [Navigator] that is attached to the [containerID] is currently remembered.
     *
     * @param containerID ID of the [FragmentContainerView] to which the [Navigator] whose presence
     *   in the [Pool] will be verified is attached.
     */
    internal operator fun contains(@IdRes containerID: Int): Boolean {
      return containerID in remembrances
    }
  }

  /**
   * Adds a [Navigation.Destination.OnChangeListener] that will be notified whenever the current
   * [Navigation.Destination] changes.
   *
   * @param onDestinationChangeListener [Navigation.Destination.OnChangeListener] to be added.
   * @see removeOnDestinationChangeListener
   */
  fun addOnDestinationChangeListener(
    onDestinationChangeListener: Navigation.Destination.OnChangeListener
  ) {
    onDestinationChangeListeners.add(onDestinationChangeListener)
  }

  /**
   * Removes a previously added [Navigation.Destination.OnChangeListener], which results in it not
   * being notified of future changes.
   *
   * @param onDestinationChangeListener [Navigation.Destination.OnChangeListener] to be removed.
   * @see addOnDestinationChangeListener
   */
  fun removeOnDestinationChangeListener(
    onDestinationChangeListener: Navigation.Destination.OnChangeListener
  ) {
    onDestinationChangeListeners.remove(onDestinationChangeListener)
  }

  /**
   * Navigates to the [Fragment] destination provided by the result of [navigation].
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Navigation.Destination]'s and that of the provided by the result of [navigation] are the
   *   same.
   * @param navigation Defines where to navigate to.
   * @see allowingDuplication
   * @see disallowingDuplication
   * @see Navigation.to
   */
  fun <T : Fragment> navigate(
    transition: Transition,
    duplication: Duplication = allowingDuplication(),
    navigation: Navigation<T>.() -> Navigation.Destination.Provider<T>
  ) {
    val previousRoute = fragmentManager.fragments.lastOrNull()?.tag
    val destination = Navigation<T>().navigation().provide()
    val canNavigate = duplication.canNavigate(previousRoute, currentRoute = destination.route)
    if (canNavigate) {
      unconditionallyNavigate(transition, destination)
    }
  }

  /** Pops the back stack. */
  fun pop() {
    fragmentManager.popBackStack()
  }

  /**
   * Navigates to the [destination] without checking if we're allowed to do so.
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param destination [Navigation.Destination] to navigate to.
   */
  private fun unconditionallyNavigate(
    transition: Transition,
    destination: Navigation.Destination<*>
  ) {
    fragmentManager.commit(allowStateLoss = true) {
      addToBackStack(null)
      setTransition(transition.value)
      add(containerID, destination.target(), destination.route)
    }
    fragmentManager.executePendingTransactions()
    onDestinationChangeListeners.forEach { it.onChange(destination) }
  }
}

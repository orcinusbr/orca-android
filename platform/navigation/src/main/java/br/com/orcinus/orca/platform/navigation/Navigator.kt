/*
 * Copyright © 2023–2024 Orcinus
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
import androidx.annotation.Discouraged
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.orcinus.orca.platform.navigation.destination.DestinationFragment
import br.com.orcinus.orca.platform.navigation.destination.FragmentProvisioningScope
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
  /** [OnNavigationListener]s that are currently listening to navigations. */
  internal val onNavigationListeners = mutableListOf<OnNavigationListener>()

  /** Defines the [Destination] that will provide the [Fragment] which is the destination. */
  @Discouraged(
    "Providing a non-`DestinationFragment` is highly discouraged, since it is a requirement of " +
      "`View`-`Navigator` integration APIs for it to be uniquely identified. When navigating, " +
      "prefer calling `Navigator.navigateToDestinationFragment` instead."
  )
  class Navigation private constructor() {
    /**
     * Target navigation site.
     *
     * @param T [Fragment] to be navigated to.
     * @param route Path by which the [fragment] can be retrieved.
     * @param fragment Returns the [Fragment] of this [Destination].
     */
    data class Destination<T : Fragment>(val route: String, val fragment: () -> T)
  }

  /**
   * Listener to be notified when a [Fragment] gets navigated to.
   *
   * @see onNavigation
   */
  fun interface OnNavigationListener {
    /**
     * Callback that gets called when navigation to a [Fragment] is performed.
     *
     * @param fragment [Fragment] that is the destination.
     */
    fun onNavigation(fragment: Fragment)
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
        remembrances.getValue(containerID).onNavigationListeners.clear()
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
    internal fun remember(activity: FragmentActivity, @IdRes containerID: Int): Navigator {
      val navigator = Navigator(activity.supportFragmentManager, containerID)
      val lifecycleObserver = RemovalLifecycleObserver(containerID)
      activity.runOnUiThread { activity.lifecycle.addObserver(lifecycleObserver) }
      remembrances[containerID] = navigator
      return navigator
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
   * Adds an [OnNavigationListener] that will be notified whenever navigation to a [Fragment] is
   * performed.
   *
   * @param onNavigationListener [OnNavigationListener] to be added.
   * @see removeOnNavigationListener
   */
  fun addOnNavigationListener(onNavigationListener: OnNavigationListener) {
    onNavigationListeners.add(onNavigationListener)
  }

  /**
   * Removes a previously added [OnNavigationListener], which results in it not being notified of
   * future navigations.
   *
   * @param onNavigationListener [OnNavigationListener] to be removed.
   * @see addOnNavigationListener
   */
  fun removeOnNavigationListener(onNavigationListener: OnNavigationListener) {
    onNavigationListeners.remove(onNavigationListener)
  }

  /**
   * Navigates to the [fragment].
   *
   * @param T [DestinationFragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [DestinationFragment]'s ID and that of the given one are the same.
   * @param fragment [DestinationFragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  fun <T : DestinationFragment> navigateToDestinationFragment(
    transition: Transition,
    duplication: Duplication = allowingDuplication(),
    fragment: T
  ) {
    _navigate(transition, duplication, fragment)
  }

  /**
   * Navigates to the [fragment] while allowing duplication.
   *
   * @param T [DestinationFragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param fragment [DestinationFragment] to which navigation will be performed.
   * @see allowingDuplication
   */
  fun <T : DestinationFragment> navigateToDestinationFragment(transition: Transition, fragment: T) {
    _navigate(transition, allowingDuplication(), fragment)
  }

  /**
   * Navigates to the provided [Fragment].
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment]'s ID and that of the given one are the same.
   * @param fragmentProvisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @Deprecated(
    "Navigating to a non-`DestinationFragment` is highly discouraged, since it is a requirement " +
      "of `View`-`Navigator` integration APIs for it to be uniquely identified.",
    ReplaceWith(
      "navigateToDestinationFragment(transition, duplication, fragmentProvisioning)",
      "br.com.orcinus.orca.platform.navigation.Navigator"
    )
  )
  fun navigate(
    transition: Transition,
    duplication: Duplication = allowingDuplication(),
    fragmentProvisioning: FragmentProvisioningScope<Fragment>.() -> Fragment
  ) {
    @Suppress("DEPRECATION") navigate<Fragment>(transition, duplication, fragmentProvisioning)
  }

  /**
   * Navigates to the provided [Fragment].
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment]'s ID and that of the given one are the same.
   * @param fragmentProvisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @Deprecated(
    "Navigating to a non-`DestinationFragment` is highly discouraged, since it is a requirement " +
      "of `View`-`Navigator` integration APIs for it to be uniquely identified.",
    ReplaceWith(
      "navigateToDestinationFragment<T>(transition, duplication, fragmentProvisioning)",
      "br.com.orcinus.orca.platform.navigation.Navigator"
    )
  )
  @JvmName("navigateToFragmentOfType")
  fun <T : Fragment> navigate(
    transition: Transition,
    duplication: Duplication = allowingDuplication(),
    fragmentProvisioning: FragmentProvisioningScope<T>.() -> T
  ) {
    val fragment = FragmentProvisioningScope<T>().fragmentProvisioning()
    _navigate(transition, duplication, fragment)
  }

  /**
   * Navigates to the [fragment].
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment]'s ID and that of the given one are the same.
   * @param fragment [DestinationFragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @Suppress("FunctionName")
  private fun <T : Fragment> _navigate(
    transition: Transition,
    duplication: Duplication,
    fragment: T
  ) {
    val currentID = fragmentManager.fragments.lastOrNull()?.id
    val nextID = if (fragment is DestinationFragment) fragment.id() else fragment.id
    val canNavigate = duplication.canNavigate(currentID, nextID)
    if (canNavigate) {
      navigateToUnidentifiedFragment(transition, fragment, nextID)
    }
  }

  /** Pops the back stack. */
  fun pop() {
    fragmentManager.popBackStack()
  }

  /**
   * Navigates to the unidentified [fragment], whose ID will be the given one after it's been added
   * to the container.
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param fragment [Fragment] to navigate to.
   * @param id ID that has been computed when the [fragment] is a [DestinationFragment] or the
   *   [Fragment]'s own immutable one.
   * @see Fragment.getId
   */
  private fun navigateToUnidentifiedFragment(transition: Transition, fragment: Fragment, id: Int) {
    fragmentManager.commit(allowStateLoss = true) {
      addToBackStack(null)
      setTransition(transition.value)
      add(containerID, fragment, fragment.tag)
      (fragment as? DestinationFragment)?.setId(id)
      onNavigationListeners.forEach { it.onNavigation(fragment) }
    }
    fragmentManager.executePendingTransactions()
  }
}

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
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.orcinus.orca.ext.grammar.IndefiniteArticle
import br.com.orcinus.orca.platform.navigation.duplication.Duplication
import br.com.orcinus.orca.platform.navigation.duplication.allowingDuplication
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.transition.Transition
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

/**
 * Navigates to [Fragment]s through [navigateOrThrow].
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
   * Navigates to the provided [Fragment], allowing for duplication.
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   */
  @OptIn(ExperimentalContracts::class)
  inline fun <reified T : Fragment> navigate(
    transition: Transition,
    noinline provisioning: () -> T
  ) {
    contract { callsInPlace(provisioning, InvocationKind.EXACTLY_ONCE) }
    navigate(transition, T::class, provisioning)
  }

  /**
   * Navigates to the provided [Fragment], taking into consideration whether duplication is allowed
   * and ignoring this request when it isn't and the current [Fragment] at which the
   * [fragmentManager] is a [T] (the same type of the one that has been provided).
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment] and the given one have the same type.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @OptIn(ExperimentalContracts::class)
  inline fun <reified T : Fragment> navigate(
    transition: Transition,
    duplication: Duplication,
    noinline provisioning: () -> T
  ) {
    contract { callsInPlace(provisioning, InvocationKind.EXACTLY_ONCE) }
    navigate(transition, duplication, T::class, provisioning)
  }

  /**
   * Navigates to the provided [Fragment], allowing for duplication.
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param fragmentClass [KClass] of the [Fragment] being specified, to which that of the current
   *   one will be compared.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   */
  @OptIn(ExperimentalContracts::class)
  fun <T : Fragment> navigate(
    transition: Transition,
    fragmentClass: KClass<T>,
    provisioning: () -> T
  ) {
    contract { callsInPlace(provisioning, InvocationKind.EXACTLY_ONCE) }
    navigateDependingOnDuplication(transition, allowingDuplication(), fragmentClass, provisioning)
  }

  /**
   * Navigates to the provided [Fragment], taking into consideration whether duplication is allowed
   * and ignoring this request when it isn't and the [fragmentClass] equals to that of the current
   * one.
   *
   * @param T [Fragment] to navigate to.
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment] and the given one have the same type.
   * @param fragmentClass [KClass] of the [Fragment] being specified, to which that of the current
   *   one will be compared.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @OptIn(ExperimentalContracts::class)
  fun <T : Fragment> navigate(
    transition: Transition,
    duplication: Duplication,
    fragmentClass: KClass<T>,
    provisioning: () -> T
  ) {
    contract { callsInPlace(provisioning, InvocationKind.EXACTLY_ONCE) }

    @Suppress("DiscouragedApi")
    navigateOrThrow(transition, duplication, fragmentClass, provisioning as () -> Fragment)
  }

  /**
   * Unsafely navigates to the provided [Fragment], taking into consideration whether duplication is
   * allowed and ignoring this request when it isn't and the [fragmentClass] equals to that of the
   * current one.
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment] and the given one have the same type.
   * @param fragmentClass [KClass] of the [Fragment] being specified, to which that of the current
   *   one will be compared.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @throws IllegalArgumentException If [provisioning] returns a [Fragment] whose [KClass] doesn't
   *   match the given [fragmentClass].
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  @Discouraged(
    "It is safer to navigate by calling the type-parametrized version of this method, since an " +
      "`IllegalArgumentException` will be thrown if the specified `fragmentClass` differs from " +
      "that of the `Fragment` returned by `provisioning` for maintenance of consistency. For a " +
      "more detailed explanation, refer to the documentation."
  )
  @OptIn(ExperimentalContracts::class)
  @Throws(IllegalArgumentException::class)
  fun navigateOrThrow(
    transition: Transition,
    duplication: Duplication,
    fragmentClass: KClass<out Fragment>,
    provisioning: () -> Fragment
  ) {
    contract { callsInPlace(provisioning, InvocationKind.EXACTLY_ONCE) }
    navigateDependingOnDuplication(transition, duplication, fragmentClass) {
      provisioning().also {
        require(it::class == fragmentClass) {
          "Navigation to ${it::class.simpleName} was requested but expected " +
            "${IndefiniteArticle.of(fragmentClass.simpleName ?: return@also)} " +
            "${fragmentClass.simpleName}."
        }
      }
    }
  }

  /** Pops the back stack. */
  fun pop() {
    fragmentManager.popBackStack()
  }

  /**
   * Navigates to the provided [Fragment], taking into consideration whether duplication is allowed
   * and ignoring this request when the [KClass] of the current [Fragment] equals to that of the
   * given one.
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param duplication Indicates whether the navigation should be performed even if the current
   *   [Fragment] and the given one have the same type.
   * @param fragmentClass [KClass] of the [Fragment] being specified, to which that of the current
   *   one will be compared.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   * @see allowingDuplication
   * @see disallowingDuplication
   */
  private fun navigateDependingOnDuplication(
    transition: Transition,
    duplication: Duplication,
    fragmentClass: KClass<out Fragment>,
    provisioning: () -> Fragment
  ) {
    val currentFragmentClass = fragmentManager.fragments.lastOrNull()?.let { it::class }
    val canNavigate = duplication.canNavigate(currentFragmentClass, fragmentClass)
    if (canNavigate) {
      navigateSynchronously(transition, provisioning)
    }
  }

  /**
   * Requests navigation to the specified [Fragment] and waits for the underlying
   * [FragmentTransaction] to be executed.
   *
   * @param transition [Transition] with which the navigation will be animated.
   * @param provisioning Provides the [Fragment] to which navigation will be performed.
   */
  private fun navigateSynchronously(transition: Transition, provisioning: () -> Fragment) {
    fragmentManager.commit {
      val fragment = provisioning()
      addToBackStack(null)
      setTransition(transition.value)
      add(containerID, fragment, fragment.tag)
      onNavigationListeners.forEach { it.onNavigation(fragment) }
    }
    fragmentManager.executePendingTransactions()
  }
}

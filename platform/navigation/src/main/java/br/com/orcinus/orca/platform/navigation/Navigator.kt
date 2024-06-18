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

import android.app.Activity
import android.view.View
import androidx.annotation.Discouraged
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
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
 * Navigates to [Fragment]s.
 *
 * @property fragmentManager [FragmentManager] that adds [Fragment]s to the [FragmentContainerView].
 * @property backStack [BackStack] to which [Fragment]s are added.
 * @property containerID ID of the [FragmentContainerView] to which [Fragment]s will be added.
 * @see navigate
 * @see navigateOrThrow
 */
class Navigator
private constructor(
  private val fragmentManager: FragmentManager,
  private val backStack: BackStack,
  @IdRes private val containerID: Int
) {
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
    fragmentManager.commit(allowStateLoss = true) {
      val fragment = provisioning()
      println(backStack)
      addToBackStack(backStack.name)
      setTransition(transition.value)
      add(containerID, fragment, fragment.tag)
    }
    fragmentManager.executePendingTransactions()
  }

  companion object {
    /**
     * Creates a [Navigator].
     *
     * **NOTE**: Because the [FragmentContainerView] that the [activity] holds needs to have an ID
     * for the [Navigator] to work properly, one is automatically generated and assigned to it if it
     * doesn't already have one.
     *
     * @param activity [FragmentActivity] from which both the [FragmentManager] and the ID of the
     *   [FragmentContainerView] are obtained.
     * @param backStack [BackStack] to which [Fragment]s are added.
     * @throws IllegalStateException If no [FragmentContainerView] is found.
     */
    @Throws(IllegalStateException::class)
    fun create(
      activity: FragmentActivity,
      backStack: BackStack = BackStack.named(activity.componentName.className)
    ): Navigator {
      return Navigator(activity.supportFragmentManager, backStack, getContainerID(activity))
    }

    /**
     * Creates a [Navigator].
     *
     * **NOTE**: Because the [FragmentContainerView] that this [Fragment]'s [FragmentActivity] holds
     * needs to have an ID for the [Navigator] to work properly, one is automatically generated and
     * assigned to it if it doesn't already have one.
     *
     * @param fragment [Fragment] from whose [FragmentActivity] both the [FragmentManager] and the
     *   ID of the [FragmentContainerView] are obtained.
     * @param backStack [BackStack] to which [Fragment]s are added.
     * @throws IllegalStateException If the [fragment] isn't attached to a [FragmentActivity] or no
     *   [FragmentContainerView] is found.
     */
    @Throws(IllegalStateException::class)
    fun create(
      fragment: Fragment,
      backStack: BackStack = BackStack.named(fragment::class.java.name)
    ): Navigator {
      return Navigator(fragment.parentFragmentManager, backStack, getContainerID(fragment))
    }

    /**
     * Obtains the ID of the [FragmentContainerView] (generating one for it if it hasn't already
     * been identified) contained by the [fragment]'s [FragmentActivity].
     *
     * @param fragment [Fragment] from which the [FragmentContainerView] will be obtained.
     * @throws IllegalStateException If the [fragment] isn't attached to a [FragmentActivity] or no
     *   [FragmentContainerView] is found.
     */
    @IdRes
    @Throws(IllegalStateException::class)
    private fun getContainerID(fragment: Fragment): Int {
      val activity = fragment.requireActivity()
      return getContainerID(activity)
    }

    /**
     * Obtains the ID of the [FragmentContainerView] (generating one for it if it hasn't already
     * been identified) contained by the [FragmentActivity].
     *
     * @param activity [FragmentActivity] from which the [FragmentContainerView] will be obtained.
     * @throws IllegalStateException If no [FragmentContainerView] is found.
     */
    @IdRes
    @Throws(IllegalStateException::class)
    private fun getContainerID(activity: Activity): Int {
      return activity.content
        .get<FragmentContainerView>(isInclusive = false)
        .also(View::identify)
        .id
    }
  }
}

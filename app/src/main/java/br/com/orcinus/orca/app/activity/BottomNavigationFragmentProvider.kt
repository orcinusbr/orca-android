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

package br.com.orcinus.orca.app.activity

import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.feature.feed.FeedFragment
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsFragment
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.feature.settings.SettingsFragment
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.transition.suddenly

/** Provides a [Fragment] to which each bottom navigation tab in the [OrcaActivity] navigates. */
internal enum class BottomNavigationFragmentProvider {
  /** Provider of a [FeedFragment]. */
  FEED {
    override val id = R.id.feed

    override suspend fun provide(
      backStack: BackStack,
      authenticationLock: SomeAuthenticationLock
    ): FeedFragment {
      return authenticationLock.scheduleUnlock { FeedFragment(backStack, it.id) }
    }
  },

  /** Provider of a [Fragment] with the details of the profile of the authenticated [Actor]. */
  PROFILE_DETAILS {
    override val id = R.id.profile_details

    override suspend fun provide(
      backStack: BackStack,
      authenticationLock: SomeAuthenticationLock
    ): ProfileDetailsFragment {
      return authenticationLock.scheduleUnlock {
        ProfileDetailsFragment(backStack, BackwardsNavigationState.Unavailable, it.id)
      }
    }
  },

  /** Provider of a [SettingsFragment]. */
  SETTINGS {
    override val id = R.id.settings

    override suspend fun provide(
      backStack: BackStack,
      authenticationLock: SomeAuthenticationLock
    ): SettingsFragment {
      return SettingsFragment()
    }
  };

  /**
   * ID of the bottom navigation tab.
   *
   * @see MenuItem.getItemId
   */
  @get:IdRes protected abstract val id: Int

  /**
   * Creates and returns the [Fragment] to which the bottom navigation tab is related to and to
   * which navigation is to be performed whenever it is clicked. The tab to which this provider
   * refers is the one identified with the specified [id].
   *
   * @param backStack [BackStack] with which the [Fragment] is created.
   * @param authenticationLock Requires authentication for creating the [Fragment].
   */
  protected abstract suspend fun provide(
    backStack: BackStack,
    authenticationLock: SomeAuthenticationLock
  ): Fragment

  companion object {
    /**
     * Navigates to the [Fragment] associated to the specified bottom navigation tab.
     *
     * @param navigator [Navigator] by which navigation will be performed.
     * @param backStack [BackStack] to which the [Fragment] will be added.
     * @param authenticationLock Requires authentication for creating the [Fragment].
     * @param id ID of the bottom navigation tab.
     * @see MenuItem.getItemId
     */
    suspend fun navigate(
      navigator: Navigator,
      backStack: BackStack,
      authenticationLock: SomeAuthenticationLock,
      @IdRes id: Int
    ) {
      val provider = entries.find { it.id == id } ?: throw NoSuchElementException("$id")
      val fragment = provider.provide(backStack, authenticationLock)

      @Suppress("DiscouragedApi")
      navigator.navigateOrThrow(suddenly(), disallowingDuplication(), fragment::class) { fragment }
    }
  }
}

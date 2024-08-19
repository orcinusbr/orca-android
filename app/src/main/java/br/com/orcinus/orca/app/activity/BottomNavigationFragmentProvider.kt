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

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.feature.feed.FeedFragment
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsFragment
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.feature.settings.SettingsFragment
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.transition.suddenly
import br.com.orcinus.orca.std.injector.Injector

internal enum class BottomNavigationFragmentProvider {
  FEED {
    override val id = R.id.feed

    override suspend fun provide(backStack: BackStack): FeedFragment {
      return authenticationLock.scheduleUnlock { FeedFragment(backStack, it.id) }
    }
  },
  PROFILE_DETAILS {
    override val id = R.id.profile_details

    override suspend fun provide(backStack: BackStack): ProfileDetailsFragment {
      return authenticationLock.scheduleUnlock {
        ProfileDetailsFragment(backStack, BackwardsNavigationState.Unavailable, it.id)
      }
    }
  },
  SETTINGS {
    override val id = R.id.settings

    override suspend fun provide(backStack: BackStack): SettingsFragment {
      return SettingsFragment()
    }
  };

  @get:IdRes protected abstract val id: Int

  protected val authenticationLock
    get() = Injector.from<CoreModule>().authenticationLock()

  protected abstract suspend fun provide(backStack: BackStack): Fragment

  companion object {
    suspend fun navigate(navigator: Navigator, backStack: BackStack, @IdRes id: Int) {
      val provider = entries.find { it.id == id } ?: throw NoSuchElementException("$id")
      val fragment = provider.provide(backStack)

      @Suppress("DiscouragedApi")
      navigator.navigateOrThrow(suddenly(), disallowingDuplication(), fragment::class) { fragment }
    }
  }
}

/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.app.navigation

import androidx.annotation.IdRes
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.authenticationLock
import com.jeanbarrossilva.orca.feature.feed.FeedFragment
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.feature.settings.SettingsFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.disallowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly
import com.jeanbarrossilva.orca.std.injector.Injector

internal enum class BottomNavigation {
  FEED {
    override val id = R.id.feed

    override suspend fun getDestination(): Navigator.Navigation.Destination<*> {
      return authenticationLock.requestUnlock {
        Navigator.Navigation.Destination(FeedFragment.ROUTE) { FeedFragment(it.id) }
      }
    }
  },
  PROFILE_DETAILS {
    override val id = R.id.profile_details

    override suspend fun getDestination(): Navigator.Navigation.Destination<*> {
      return authenticationLock.requestUnlock {
        Navigator.Navigation.Destination(ProfileDetailsFragment.createRoute(it.id)) {
          ProfileDetailsFragment(BackwardsNavigationState.Unavailable, it.id)
        }
      }
    }
  },
  SETTINGS {
    override val id = R.id.settings

    override suspend fun getDestination(): Navigator.Navigation.Destination<*> {
      return Navigator.Navigation.Destination("settings", ::SettingsFragment)
    }
  };

  @get:IdRes protected abstract val id: Int

  protected val authenticationLock
    get() = Injector.from<CoreModule>().authenticationLock()

  protected abstract suspend fun getDestination(): Navigator.Navigation.Destination<*>

  companion object {
    suspend fun navigate(navigator: Navigator, @IdRes id: Int) {
      val value = values().find { it.id == id } ?: throw NoSuchElementException("$id")
      val destination = value.getDestination()
      navigator.navigate(suddenly(), disallowingDuplication()) {
        to(destination.route, destination.target)
      }
    }
  }
}

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

package com.jeanbarrossilva.orca.feature.profiledetails.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class ProfileDetailsActivity : SingleFragmentActivity() {
  override val route = "profile-details"

  override fun NavGraphBuilder.add() {
    fragment<ProfileDetailsFragment>()
  }

  companion object {
    fun getIntent(backwardsNavigationState: BackwardsNavigationState, id: String): Intent {
      val context = InstrumentationRegistry.getInstrumentation().context
      return Intent<ProfileDetailsActivity>(
        context,
        ProfileDetailsFragment.BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState,
        ProfileDetailsFragment.ID_KEY to id
      )
    }
  }
}

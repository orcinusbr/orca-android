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

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState

/**
 * Launches a [ProfileDetailsActivity].
 *
 * @param id ID of the [Profile] whose details will be shown.
 */
internal fun launchProfileDetailsActivity(
  id: String = Profile.sample.id
): ActivityScenario<ProfileDetailsActivity> {
  val intent = ProfileDetailsActivity.getIntent(BackwardsNavigationState.Unavailable, id)
  return launchActivity(intent)
}

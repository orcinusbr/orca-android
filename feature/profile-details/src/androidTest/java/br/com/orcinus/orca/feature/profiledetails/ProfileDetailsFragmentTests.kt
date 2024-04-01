/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.feature.profiledetails

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.feature.profiledetails.test.TestProfileDetailsModule
import br.com.orcinus.orca.feature.profiledetails.test.isFollowingType
import br.com.orcinus.orca.feature.profiledetails.test.launchProfileDetailsActivity
import br.com.orcinus.orca.feature.profiledetails.test.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import com.jeanbarrossilva.loadable.placeholder.test.isLoading
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsFragmentTests {
  private val injectorRule = InjectorTestRule {
    register<ProfileDetailsModule>(TestProfileDetailsModule)
  }
  private val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)
  private val composeRule = createEmptyComposeRule()

  @get:Rule
  val ruleChain: RuleChain =
    RuleChain.outerRule(injectorRule).around(sampleInstanceRule).around(composeRule)

  @Test
  fun unfollowsFollowedProfileWhenClickingActionButton() {
    val profile = FollowableProfile.sample
    Instance.sample.profileWriter.insert(profile)
    if (!profile.follow.isFollowingType) {
      runTest { profile.toggleFollow() }
    }
    launchProfileDetailsActivity(profile.id).use {
      composeRule
        .onNodeWithTag(ProfileDetails.Followable.MAIN_ACTION_BUTTON_TAG)
        .performClick()
        .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
    }
  }

  @Test
  fun loadsPosts() {
    launchProfileDetailsActivity().use {
      @OptIn(ExperimentalTestApi::class)
      composeRule.waitUntilDoesNotExist(isLoading(), timeoutMillis = 4.seconds.inWholeMilliseconds)
    }
  }
}

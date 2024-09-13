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

package br.com.orcinus.orca.feature.profiledetails

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.feature.profiledetails.test.UnnavigableProfileDetailsModule
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ProfileDetailsFragmentTests {
  private val instance =
    SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
      .withDefaultProfiles()
      .build()
  private val followService = SampleFollowService(instance.profileProvider)

  @get:Rule
  val injectorRule = InjectorTestRule {
    register<ProfileDetailsModule>(
      UnnavigableProfileDetailsModule(
        instance.profileProvider,
        followService,
        instance.postProvider
      )
    )
  }
  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun unfollowsFollowedProfileWhenClickingActionButton() {
    val profile = instance.profileProvider.provideCurrent<FollowableProfile<*>>()
    if (!profile.follow.isFollowingType) {
      runTest { followService.toggle(profile.id, profile.follow) }
    }
    launchFragmentInNavigationContainer {
        ProfileDetailsFragment(
          BackStack.named(ProfileDetailsFragment::class.java.name),
          BackwardsNavigationState.Unavailable,
          profile.id
        )
      }
      .use {
        composeRule
          .onNodeWithTag(ProfileDetails.Followable.MainActionButtonTag)
          .performClick()
          .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
      }
  }
}

package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.feature.profiledetails.test.ProfileDetailsActivity
import com.jeanbarrossilva.orca.feature.profiledetails.test.TestProfileDetailsModule
import com.jeanbarrossilva.orca.feature.profiledetails.test.isFollowingType
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsFragmentTests {
  private val injectorRule = InjectorTestRule {
    register<ProfileDetailsModule>(TestProfileDetailsModule)
  }
  private val sampleInstanceRule = SampleInstanceTestRule()
  private val time4JRule = Time4JTestRule()
  private val composeRule = createEmptyComposeRule()

  @get:Rule
  val ruleChain: RuleChain =
    RuleChain.outerRule(injectorRule)
      .around(sampleInstanceRule)
      .around(time4JRule)
      .around(composeRule)

  @Test
  fun unfollowsFollowedProfileWhenClickingActionButton() {
    val profile = FollowableProfile.sample
    Instance.sample.profileWriter.insert(profile)
    if (!profile.follow.isFollowingType) {
      runTest { profile.toggleFollow() }
    }
    launchActivity<ProfileDetailsActivity>(
        ProfileDetailsActivity.getIntent(BackwardsNavigationState.Unavailable, profile.id)
      )
      .use {
        composeRule
          .onNodeWithTag(ProfileDetails.Followable.MAIN_ACTION_BUTTON_TAG)
          .performClick()
          .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
      }
  }
}

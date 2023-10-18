package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.feature.profiledetails.test.ProfileDetailsActivity
import com.jeanbarrossilva.orca.feature.profiledetails.test.TestProfileDetailsModule
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsFragmentTests {
  private val injectorRule = InjectorTestRule {
    register<ProfileDetailsModule>(TestProfileDetailsModule)
  }
  private val sampleCoreRule = SampleCoreTestRule()
  private val time4JRule = Time4JTestRule()
  private val composeRule = createEmptyComposeRule()

  @get:Rule
  val ruleChain: RuleChain =
    RuleChain.outerRule(injectorRule).around(sampleCoreRule).around(time4JRule).around(composeRule)

  @Test
  fun unfollowsFollowedProfileWhenClickingActionButton() {
    SampleProfileWriter.insert(FollowableProfile.sample)
    launchActivity<ProfileDetailsActivity>(
        ProfileDetailsActivity.getIntent(
          BackwardsNavigationState.Unavailable,
          FollowableProfile.sample.id
        )
      )
      .use {
        composeRule
          .onNodeWithTag(ProfileDetails.Followable.MAIN_ACTION_BUTTON_TAG)
          .performScrollTo()
          .performClick()
          .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
      }
  }
}

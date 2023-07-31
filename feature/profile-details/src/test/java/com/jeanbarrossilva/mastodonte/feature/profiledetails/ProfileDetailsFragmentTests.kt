package com.jeanbarrossilva.mastodonte.feature.profiledetails

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.test.SampleProfileWriterTestRule
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.mastodonte.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodonte.feature.profiledetails.test.ProfileDetailsActivity
import com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ProfileDetailsFragmentTests {
    private val sampleProfileDaoRule = SampleProfileWriterTestRule()
    private val time4JRule = Time4JTestRule()
    private val composeRule = createEmptyComposeRule()

    @get:Rule
    val ruleChain: RuleChain = RuleChain
        .outerRule(sampleProfileDaoRule)
        .around(time4JRule)
        .around(composeRule)

    @Test
    fun `GIVEN a followed profile button WHEN clicking the toggle follow button THEN it's unfollowed`() { // ktlint-disable max-line-length
        SampleProfileWriter.insert(FollowableProfile.sample)
        Robolectric
            .buildActivity(
                ProfileDetailsActivity::class.java,
                ProfileDetailsActivity
                    .getIntent(BackwardsNavigationState.Unavailable, FollowableProfile.sample.id)
            )
            .setup()
            .use {
                composeRule
                    .onNodeWithTag(ProfileDetails.Followable.MAIN_ACTION_BUTTON_TAG)
                    .performClick()
                    .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
            }
    }
}

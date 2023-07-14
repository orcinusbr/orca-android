package com.jeanbarrossilva.mastodonte.feature.profiledetails

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileWriter
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.test.SampleProfileWriterTestRule
import com.jeanbarrossilva.mastodonte.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodonte.feature.profiledetails.test.ProfileDetailsActivityScenarioRule
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TOOT_PREVIEW_FAVORITE_COUNT_STAT_TAG
import com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.onTootPreviews
import com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsFragmentTests {
    private val sampleProfileDaoRule = SampleProfileWriterTestRule()
    private val time4JRule = Time4JTestRule()
    private val activityScenarioRule = ProfileDetailsActivityScenarioRule(
        BackwardsNavigationState.Unavailable,
        FollowableProfile.sample.id
    )
    private val composeRule = createEmptyComposeRule()

    @get:Rule
    val ruleChain: RuleChain = RuleChain
        .outerRule(sampleProfileDaoRule)
        .around(time4JRule)
        .around(composeRule)
        .around(activityScenarioRule)

    @Test
    fun togglesFollowStatusOnButtonClick() {
        SampleProfileWriter.insert(FollowableProfile.sample)
        composeRule
            .onNodeWithTag(ProfileDetails.Followable.MAIN_ACTION_BUTTON_TAG)
            .performClick()
            .assertTextEquals(ProfileDetails.Followable.Status.UNFOLLOWED.label)
    }

    @Test
    fun togglesTootFavoriteStatusOnFavoriteButtonClick() {
        composeRule
            .onTootPreviews()
            .onFirst()
            .onChildren()
            .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_COUNT_STAT_TAG))
            .performClick()
            .run {
                composeRule.waitUntil {
                    fetchSemanticsNode().config[SemanticsProperties.Selected]
                }
            }
    }
}

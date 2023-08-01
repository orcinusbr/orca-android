package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootWriter
import com.jeanbarrossilva.orca.feature.feed.test.FeedActivity
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedFragmentTests {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun `GIVEN a toot WHEN marking it as favorite THEN it's favorited`() {
        runTest {
            SampleTootWriter
                .updateFavorite(Profile.sample.getToots(page = 0).first().first().id, false)
        }
        Robolectric
            .buildActivity(FeedActivity::class.java, FeedActivity.getIntent(Profile.sample.id))
            .setup()
            .use {
                composeRule
                    .onTootPreviews()
                    .onFirst()
                    .onChildren()
                    .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_STAT_TAG))
                    .performClick()
                    .assertIsSelected()
            }
    }
}

package com.jeanbarrossilva.mastodonte.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.samples
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.onTootPreviews
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `GIVEN a toot's favorite stat WHEN clicking it THEN the callback is called`() {
        var hasCallbackBeenCalled = false
        composeRule.setContent {
            MastodonteTheme {
                Feed(
                    Toot.samples,
                    onSearch = { },
                    onFavorite = { hasCallbackBeenCalled = true },
                    onReblog = { },
                    onShare = { },
                    onTootClick = { },
                    onNext = { },
                    onComposition = { }
                )
            }
        }
        composeRule
            .onTootPreviews()
            .onFirst()
            .onChildren()
            .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_STAT_TAG))
            .performClick()
        assertTrue(hasCallbackBeenCalled)
    }
}

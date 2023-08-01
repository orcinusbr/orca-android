package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
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
            OrcaTheme {
                Feed(
                    TootPreview.samples.toSerializableList().toListLoadable(),
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

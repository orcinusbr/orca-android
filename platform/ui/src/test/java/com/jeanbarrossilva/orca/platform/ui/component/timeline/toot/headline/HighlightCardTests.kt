package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline.onHeadlineCard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class HighlightCardTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `GIVEN a highlight card WHEN clicking on it THEN its callback is run`() {
        var hasCallbackBeenRun = false
        composeRule.setContent {
            OrcaTheme {
                HeadlineCard(onClick = { hasCallbackBeenRun = true })
            }
        }
        composeRule.onHeadlineCard().performClick()
        assertTrue(hasCallbackBeenRun)
    }
}

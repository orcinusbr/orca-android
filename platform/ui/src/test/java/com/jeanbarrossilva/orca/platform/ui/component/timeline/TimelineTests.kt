package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class TimelineTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsEmptyMessageWhenListLoadableIsEmpty() {
        composeRule.setContent {
            OrcaTheme {
                Timeline(ListLoadable.Empty())
            }
        }
        composeRule.onNodeWithTag(EMPTY_TIMELINE_MESSAGE_TAG).assertIsDisplayed()
    }
}

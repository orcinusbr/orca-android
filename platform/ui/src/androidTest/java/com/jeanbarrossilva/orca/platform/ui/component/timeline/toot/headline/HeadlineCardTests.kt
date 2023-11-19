package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline.onHeadlineCard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class HeadlineCardTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClicked() {
    var hasCallbackBeenRun = false
    composeRule.setContent { AutosTheme { HeadlineCard(onClick = { hasCallbackBeenRun = true }) } }
    composeRule.onHeadlineCard().performClick()
    assertTrue(hasCallbackBeenRun)
  }
}

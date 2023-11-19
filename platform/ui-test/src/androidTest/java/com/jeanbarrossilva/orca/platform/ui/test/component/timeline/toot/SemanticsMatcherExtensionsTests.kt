package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsTootPreviewNode() {
    composeRule.setContent { AutosTheme { TootPreview() } }
    composeRule.onNode(isTootPreview()).assertIsDisplayed()
  }
}

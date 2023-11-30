package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.refresh

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.Refresh
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsNodeThatIsInProgress() {
    composeRule.setContent { Timeline(onNext = {}, refresh = Refresh.Indefinite) {} }
    composeRule.onNode(isInProgress()).assertIsDisplayed()
  }
}

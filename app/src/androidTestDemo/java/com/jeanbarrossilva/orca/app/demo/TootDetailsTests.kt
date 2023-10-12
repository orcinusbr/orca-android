package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.ok
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline.onHeadlineCards
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class TootDetailsTests {
  @get:Rule val intentsRule = IntentsRule()

  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesToTootHighlight() {
    var hasNavigated = false
    intending(browsesTo("${Highlight.sample.url}")).ok { hasNavigated = true }
    composeRule.onHeadlineCards().onFirst().performClick()
    assertTrue(hasNavigated)
  }
}

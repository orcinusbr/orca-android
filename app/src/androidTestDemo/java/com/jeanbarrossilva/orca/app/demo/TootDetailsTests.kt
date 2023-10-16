package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.demo.test.PlatformDialogDismissalTestRule
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.ok
import com.jeanbarrossilva.orca.app.demo.test.waitUntil
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline.onHeadlineCards
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class TootDetailsTests {
  private val intentsRule = IntentsRule()
  private val composeRule = createAndroidComposeRule<DemoOrcaActivity>()
  private val platformDialogDismissalRule = PlatformDialogDismissalTestRule()

  @get:Rule
  val ruleChain: RuleChain? =
    RuleChain.outerRule(intentsRule).around(composeRule).around(platformDialogDismissalRule)

  @Test
  fun navigatesToTootHighlight() {
    var hasNavigated = false
    intending(browsesTo("${Highlight.sample.url}")).ok { hasNavigated = true }
    composeRule.onHeadlineCards().onFirst().performClick()
    waitUntil { hasNavigated }
  }
}

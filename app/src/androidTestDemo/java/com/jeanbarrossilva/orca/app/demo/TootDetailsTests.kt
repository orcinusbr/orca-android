package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.demo.test.PlatformDialogDismissalTestRule
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.performScrollToTootPreviewWithHeadlineCard
import com.jeanbarrossilva.orca.app.demo.test.respondWithOK
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
    val matcher = browsesTo("${Highlight.sample.url}")
    intending(matcher).respondWithOK()
    composeRule.performScrollToTootPreviewWithHeadlineCard()
    composeRule.onHeadlineCards().onFirst().performClick()
    intended(matcher)
  }
}

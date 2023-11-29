package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithHeadlineCard
import com.jeanbarrossilva.orca.app.demo.test.respondWithOK
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.headline.onHeadlineCards
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class PostDetailsTests {
  private val intentsRule = IntentsRule()
  private val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @get:Rule val ruleChain: RuleChain? = RuleChain.outerRule(intentsRule).around(composeRule)

  @Test
  fun navigatesToPostHighlight() {
    val matcher = browsesTo("${Highlight.sample.url}")
    intending(matcher).respondWithOK()
    composeRule.performScrollToPostPreviewWithHeadlineCard()
    composeRule.onHeadlineCards().onFirst().performClick()
    intended(matcher)
  }
}

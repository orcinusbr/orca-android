package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FEED_FLOATING_ACTION_BUTTON_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class FeedTests {
  private val intentsRule = IntentsRule()
  private val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @get:Rule val ruleChain: RuleChain? = RuleChain.outerRule(intentsRule).around(composeRule)

  @Test
  fun navigatesToComposerOnFabClick() {
    composeRule.onNodeWithTag(FEED_FLOATING_ACTION_BUTTON_TAG).performClick()
    intended(hasComponent(ComposerActivity::class.qualifiedName))
  }
}

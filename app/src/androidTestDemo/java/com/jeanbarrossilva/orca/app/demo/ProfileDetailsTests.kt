package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.demo.test.performStartClick
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsTests {
  private val intentsRule = IntentsRule()
  private val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @get:Rule val ruleChain: RuleChain? = RuleChain.outerRule(intentsRule).around(composeRule)

  @Test
  fun navigatesToTootDetailsOnTootPreviewClick() {
    onView(withId(R.id.profile_details)).perform(click())
    composeRule.onTootPreviews().onFirst().performStartClick()
    assertIsAtFragment(composeRule.activity, TootDetailsFragment.getRoute(Toot.sample.id))
  }
}

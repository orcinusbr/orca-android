package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithHeadlineCard
import com.jeanbarrossilva.orca.app.demo.test.performStartClick
import com.jeanbarrossilva.orca.app.demo.test.respondWithOK
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.headline.onHeadlineCards
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreviews
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsTests {
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

  @Test
  fun navigatesToPostDetailsOnPostPreviewClick() {
    onView(withId(R.id.profile_details)).perform(click())
    composeRule.onPostPreviews().onFirst().performStartClick()
    assertIsAtFragment(composeRule.activity, PostDetailsFragment.getRoute(Post.samples.first().id))
  }
}

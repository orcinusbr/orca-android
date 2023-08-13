package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FEED_FLOATING_ACTION_BUTTON_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedToComposerTests {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun navigatesToComposerOnFabClick() {
        Robolectric.buildActivity(DemoOrcaActivity::class.java).setup().use {
            Intents.init()
            composeRule.onNodeWithTag(FEED_FLOATING_ACTION_BUTTON_TAG).performClick()
            intended(hasComponent(ComposerActivity::class.qualifiedName))
        }
    }
}

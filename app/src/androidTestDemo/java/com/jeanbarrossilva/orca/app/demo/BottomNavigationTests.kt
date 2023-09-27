package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class BottomNavigationTests {
    @get:Rule
    val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

    @Test
    fun navigatesOnceWhenClickingFeedItemMultipleTimes() {
        repeat(2) { onView(withId(R.id.feed)).perform(click()) }
        assertEquals(1, composeRule.activity.supportFragmentManager.fragments.size)
    }

    @Test
    fun navigatesOnceWhenClickingProfileDetailsItemMultipleTimes() {
        repeat(2) { onView(withId(R.id.profile_details)).perform(click()) }

        /*
         * We expect it to have two Fragment instances because we start with a FeedFragment,
         * and then comes the single ProfileDetailsFragment one after the navigation is
         * performed once.
         */
        assertEquals(2, composeRule.activity.supportFragmentManager.fragments.size)
    }
}

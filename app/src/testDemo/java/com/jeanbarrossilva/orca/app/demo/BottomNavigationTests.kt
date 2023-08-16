package com.jeanbarrossilva.orca.app.demo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class BottomNavigationTests {
    @Test
    fun `GIVEN the feed item WHEN clicking it multiple times THEN navigation is only performed once`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(DemoOrcaActivity::class.java).setup().use {
            repeat(2) { onView(withId(R.id.feed)).perform(click()) }
            assertEquals(1, it.get().supportFragmentManager.fragments.size)
        }
    }

    @Test
    fun `GIVEN the profile details item WHEN clicking it multiple times THEN navigation is only performed once`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(DemoOrcaActivity::class.java).setup().use {
            repeat(2) { onView(withId(R.id.profile_details)).perform(click()) }

            /*
             * We expect it to have two Fragment instances because we start with a FeedFragment,
             * and then comes the single ProfileDetailsFragment one after the navigation is
             * performed once.
             */
            assertEquals(2, it.get().supportFragmentManager.fragments.size)
        }
    }
}

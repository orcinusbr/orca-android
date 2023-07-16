package com.jeanbarrossilva.mastodonte.app

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.onTootPreviews
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ProfileDetailsToTootDetailsTests {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun navigatesToTootDetailsOnTootPreviewClick() {
        launchActivity<MastodonteActivity>().use { scenario ->
            onView(withId(R.id.profile_details)).perform(click())
            composeRule.onTootPreviews().onFirst().performClick()
            scenario.onActivity { activity ->
                assertNotNull(
                    activity.supportFragmentManager.findFragmentByTag(TootDetailsFragment.TAG)
                )
            }
        }
    }
}

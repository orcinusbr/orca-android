package com.jeanbarrossilva.orca.app.demo

import android.os.Looper
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
internal class ProfileDetailsToTootDetailsTests {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun navigatesToTootDetailsOnTootPreviewClick() {
        Robolectric.buildActivity(DemoOrcaActivity::class.java).setup().use {
            onView(withId(R.id.profile_details)).perform(click())
            composeRule.onTootPreviews().onFirst().performClick()
            shadowOf(Looper.getMainLooper()).idle()
            assertIsAtFragment(it.get(), Navigator.tagFor<TootDetailsFragment>())
        }
    }
}

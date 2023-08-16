package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.disallowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.closing
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigatorTests {
    class TestNavigationActivity : NavigationActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(FragmentContainerView(this))
        }
    }

    class DestinationFragment : Fragment() {
        companion object {
            const val ROUTE = "destination"
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it suddenly THEN it's navigated to`() {
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(suddenly()) {
                to(DestinationFragment.ROUTE, ::DestinationFragment)
            }
            assertIsAtFragment(activity, DestinationFragment.ROUTE)
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it with an open transition THEN it's navigated to`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(opening()) {
                to(DestinationFragment.ROUTE, ::DestinationFragment)
            }
            assertIsAtFragment(activity, DestinationFragment.ROUTE)
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it with a closing transition THEN it's navigated to`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(closing()) {
                to(DestinationFragment.ROUTE, ::DestinationFragment)
            }
            assertIsAtFragment(activity, DestinationFragment.ROUTE)
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it twice while allowing duplication THEN it's navigated to`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            repeat(2) {
                activity.navigator.navigate(suddenly()) {
                    to(DestinationFragment.ROUTE, ::DestinationFragment)
                }
            }
            assertEquals(2, activity.supportFragmentManager.fragments.size)
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it twice while disallowing duplication THEN navigation has only been performed once`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            repeat(2) {
                activity.navigator.navigate(suddenly(), disallowingDuplication()) {
                    to(DestinationFragment.ROUTE, ::DestinationFragment)
                }
            }
            assertEquals(1, activity.supportFragmentManager.fragments.size)
        }
    }
}

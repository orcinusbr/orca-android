package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.closing
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
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

    class DestinationFragment : Fragment()

    @Test
    fun `GIVEN a destination WHEN navigating to it suddenly THEN it's navigated to`() {
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(suddenly()) { to(::DestinationFragment) }
            assertIsAtFragment(activity, Navigator.tagFor<DestinationFragment>())
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it with an open transition THEN it's navigated to`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(opening()) { to(::DestinationFragment) }
            assertIsAtFragment(activity, Navigator.tagFor<DestinationFragment>())
        }
    }

    @Test
    fun `GIVEN a destination WHEN navigating to it with a closing transition THEN it's navigated to`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(TestNavigationActivity::class.java).setup().use {
            val activity = it.get()
            activity.navigator.navigate(closing()) { to(::DestinationFragment) }
            assertIsAtFragment(activity, Navigator.tagFor<DestinationFragment>())
        }
    }
}

package com.jeanbarrossilva.mastodonte.platform.ui.test.core

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.get
import androidx.test.core.app.launchActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class SingleFragmentActivityTests {
    abstract class TestActivity : SingleFragmentActivity() {
        var calledNavGraphIntegrityCallback: NavGraphIntegrityCallback? = null
            private set

        enum class NavGraphIntegrityCallback {
            NO_DESTINATION,
            INEQUIVALENT_DESTINATION_ROUTE,
            NON_FRAGMENT_DESTINATION,
            MULTIPLE_DESTINATIONS
        }

        override fun onNoDestination() {
            calledNavGraphIntegrityCallback = NavGraphIntegrityCallback.NO_DESTINATION
            runTest {
                /**
                 * Cancelling navGraphIntegrityInsuranceScope's Job prevents the NavGraph from being
                 * set to the empty one we've built, since the initial route given to it has to be
                 * that of an existing destination. If it isn't, which is when this callback is
                 * called, an IllegalArgumentException stating that the route should point to a
                 * direct child of the NavGraph is thrown.
                 *
                 * https://android.googlesource.com/platform/frameworks/support/+/27daece9174f19f73e32b13d4f431bec940b03fd/navigation/navigation-common/src/main/java/androidx/navigation/NavGraphNavigator.kt#59
                 **/
                navGraphIntegrityInsuranceScope.coroutineContext[Job]?.cancelAndJoin()
            }
        }

        override fun onInequivalentDestinationRoute() {
            calledNavGraphIntegrityCallback =
                NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE
        }

        override fun onNonFragmentDestination() {
            calledNavGraphIntegrityCallback = NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION
        }

        override fun onMultipleDestinations() {
            calledNavGraphIntegrityCallback = NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS
        }
    }

    class NoDestinationActivity : TestActivity() {
        override val route = "no-destination"

        override fun NavGraphBuilder.add() {
        }
    }

    class InequivalentlyRoutedDestinationActivity : TestActivity() {
        override val route = "inequivalently-routed-destination"

        override fun NavGraphBuilder.add() {
            fragment<Fragment>("🫠")
        }
    }

    class NonFragmentDestinationActivity : TestActivity() {
        override val route = "non-fragment-destination"

        class DestinationActivity : Activity()

        override fun NavGraphBuilder.add() {
            activity(this@NonFragmentDestinationActivity.route) {
                activityClass = DestinationActivity::class
            }
        }
    }

    class MultipleDestinationsActivity : TestActivity() {
        override val route = "multiple-destinations"

        override fun NavGraphBuilder.add() {
            fragment<Fragment>(this@MultipleDestinationsActivity.route)
            dialog<DialogFragment>("dialog")
        }
    }

    class PosteriorlyAddedDestinationActivity : TestActivity() {
        private lateinit var navigator: FragmentNavigator

        private val posteriorDestinationRoute
            get() = "$route/destination"

        override val route = "posteriorly-added-destination"

        override fun NavGraphBuilder.add() {
            navigator = provider[FragmentNavigator::class]
            fragment<Fragment>(this@PosteriorlyAddedDestinationActivity.route)
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            addPosteriorDestination()
            navController.navigate(posteriorDestinationRoute)
        }

        private fun addPosteriorDestination() {
            val destination = FragmentNavigatorDestinationBuilder(
                navigator,
                posteriorDestinationRoute,
                Fragment::class
            )
                .build()
            navController.graph.addDestination(destination)
        }
    }

    @Test
    fun callsOnNoDestinationCallback() {
        launchActivity<NoDestinationActivity>().onActivity {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.NO_DESTINATION,
                it.calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnInequivalentDestinationRouteCallback() {
        launchActivity<InequivalentlyRoutedDestinationActivity>().onActivity {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE,
                it.calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnNonFragmentDestinationCallback() {
        launchActivity<NonFragmentDestinationActivity>().onActivity {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION,
                it.calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnMultipleDestinationsCallback() {
        launchActivity<MultipleDestinationsActivity>().onActivity {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS,
                it.calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnMultipleDestinationsCallbackWhenNavigatingToAnotherPosteriorlyAddedDestination() {
        launchActivity<PosteriorlyAddedDestinationActivity>().onActivity {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS,
                it.calledNavGraphIntegrityCallback
            )
        }
    }
}

package com.jeanbarrossilva.mastodonte.platform.ui.test.core

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.get
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
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
            cancelNavGraphIntegrityInsuranceJob()
        }

        override fun onInequivalentDestinationRoute() {
            calledNavGraphIntegrityCallback =
                NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE
            cancelNavGraphIntegrityInsuranceJob()
        }

        override fun onNonFragmentDestination() {
            calledNavGraphIntegrityCallback = NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION
        }

        override fun onMultipleDestinations() {
            calledNavGraphIntegrityCallback = NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS
        }

        private fun cancelNavGraphIntegrityInsuranceJob() {
            runTest {
                navGraphIntegrityInsuranceJob?.cancelAndJoin()
            }
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
            fragment<Fragment>()
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
            fragment<Fragment>()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            navigateToPosteriorDestination()
        }

        private fun navigateToPosteriorDestination() {
            val destination = FragmentNavigatorDestinationBuilder(
                navigator,
                posteriorDestinationRoute,
                Fragment::class
            )
                .build()
            lifecycleScope.launch {
                navGraphIntegrityInsuranceJob?.join()
                shadowOf(Looper.getMainLooper()).idle()
                navController.graph.addDestination(destination)
                navController.navigate(posteriorDestinationRoute)
            }
        }
    }

    @Test
    fun callsOnNoDestinationCallback() {
        Robolectric.buildActivity(NoDestinationActivity::class.java).setup().use {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.NO_DESTINATION,
                it.get().calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnInequivalentDestinationRouteCallback() {
        Robolectric.buildActivity(InequivalentlyRoutedDestinationActivity::class.java).setup().use {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE,
                it.get().calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnNonFragmentDestinationCallback() {
        Robolectric.buildActivity(NonFragmentDestinationActivity::class.java).setup().use {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION,
                it.get().calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnMultipleDestinationsCallback() {
        Robolectric.buildActivity(MultipleDestinationsActivity::class.java).setup().use {
            shadowOf(Looper.getMainLooper()).idle()
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS,
                it.get().calledNavGraphIntegrityCallback
            )
        }
    }

    @Test
    fun callsOnMultipleDestinationsCallbackWhenNavigatingToAnotherPosteriorlyAddedDestination() {
        Robolectric.buildActivity(PosteriorlyAddedDestinationActivity::class.java).setup().use {
            assertEquals(
                TestActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS,
                it.get().calledNavGraphIntegrityCallback
            )
        }
    }
}

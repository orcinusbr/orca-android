package com.jeanbarrossilva.orca.platform.ui.test.core

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.get
import com.jeanbarrossilva.orca.platform.ui.test.core.test.TestSingleFragmentActivity
import com.jeanbarrossilva.orca.platform.ui.test.core.test.TestSingleFragmentActivity.Companion.assertRunNavGraphCallbackEquals
import org.junit.Test

internal class SingleFragmentActivityTests {
  class NoDestinationActivity : TestSingleFragmentActivity() {
    override val route = "no-destination"

    override fun NavGraphBuilder.add() {}
  }

  class InequivalentlyRoutedDestinationActivity : TestSingleFragmentActivity() {
    override val route = "inequivalently-routed-destination"

    override fun NavGraphBuilder.add() {
      fragment<Fragment>("🫠")
    }
  }

  class NonFragmentDestinationActivity : TestSingleFragmentActivity() {
    override val route = "non-fragment-destination"

    class DestinationActivity : Activity()

    override fun NavGraphBuilder.add() {
      activity(this@NonFragmentDestinationActivity.route) {
        activityClass = DestinationActivity::class
      }
    }
  }

  class MultipleDestinationsActivity : TestSingleFragmentActivity() {
    override val route = "multiple-destinations"

    override fun NavGraphBuilder.add() {
      fragment<Fragment>()
      dialog<DialogFragment>("dialog")
    }
  }

  class PosteriorlyAddedDestinationActivity : TestSingleFragmentActivity() {
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
      doOnNavGraphChange {
        val destination =
          FragmentNavigatorDestinationBuilder(navigator, posteriorDestinationRoute, Fragment::class)
            .build()
        navController.graph.addDestination(destination)
        navController.navigate(posteriorDestinationRoute)
      }
    }
  }

  @Test
  fun runsOnNoDestinationCallback() {
    assertRunNavGraphCallbackEquals<NoDestinationActivity>(
      TestSingleFragmentActivity.NavGraphIntegrityCallback.NO_DESTINATION
    )
  }

  @Test
  fun runsOnInequivalentDestinationRouteCallback() {
    assertRunNavGraphCallbackEquals<InequivalentlyRoutedDestinationActivity>(
      TestSingleFragmentActivity.NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE
    )
  }

  @Test
  fun runsOnNonFragmentDestinationCallback() {
    assertRunNavGraphCallbackEquals<NonFragmentDestinationActivity>(
      TestSingleFragmentActivity.NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION
    )
  }

  @Test
  fun runsOnMultipleDestinationsCallback() {
    assertRunNavGraphCallbackEquals<MultipleDestinationsActivity>(
      TestSingleFragmentActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS
    )
  }

  @Test
  fun runsOnMultipleDestinationsCallbackWhenNavigatingToAnotherPosteriorlyAddedDestination() {
    assertRunNavGraphCallbackEquals<PosteriorlyAddedDestinationActivity>(
      TestSingleFragmentActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS
    )
  }
}

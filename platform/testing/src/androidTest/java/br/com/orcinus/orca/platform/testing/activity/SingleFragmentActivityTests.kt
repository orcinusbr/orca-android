/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.testing.activity

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
import androidx.test.core.app.launchActivity
import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class SingleFragmentActivityTests {
  internal abstract class TestSingleFragmentActivity : SingleFragmentActivity() {
    var runNavGraphIntegrityCallback: NavGraphIntegrityCallback? = null
      private set

    enum class NavGraphIntegrityCallback {
      NO_DESTINATION,
      INEQUIVALENT_DESTINATION_ROUTE,
      NON_FRAGMENT_DESTINATION,
      MULTIPLE_DESTINATIONS
    }

    override fun onNoDestination() {
      runNavGraphIntegrityCallback = NavGraphIntegrityCallback.NO_DESTINATION
      cancelNavGraphIntegrityInsuranceJob()
    }

    override fun onInequivalentDestinationRoute() {
      runNavGraphIntegrityCallback = NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE
      cancelNavGraphIntegrityInsuranceJob()
    }

    override fun onNonFragmentDestination() {
      runNavGraphIntegrityCallback = NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION
    }

    override fun onMultipleDestinations() {
      runNavGraphIntegrityCallback = NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS
    }

    private fun cancelNavGraphIntegrityInsuranceJob() {
      runTest { navGraphIntegrityInsuranceJob?.cancelAndJoin() }
    }

    companion object {
      /**
       * Assertion for the [TestSingleFragmentActivity]'s [NavGraphIntegrityCallback] to get run
       * when this it is launched.
       *
       * @param T [TestSingleFragmentActivity] whose [NavGraphIntegrityCallback] will be asserted.
       */
      inline fun <reified T : TestSingleFragmentActivity> assertThatRunNavGraphCallbackOf():
        Assert<NavGraphIntegrityCallback?> {
        var callback: NavGraphIntegrityCallback? = null
        launchActivity<T>().use { scenario ->
          scenario.onActivity { activity -> callback = activity.runNavGraphIntegrityCallback }
        }
        return assertThat(callback)
      }
    }
  }

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
    TestSingleFragmentActivity.assertThatRunNavGraphCallbackOf<NoDestinationActivity>()
      .isEqualTo(TestSingleFragmentActivity.NavGraphIntegrityCallback.NO_DESTINATION)
  }

  @Test
  fun runsOnInequivalentDestinationRouteCallback() {
    TestSingleFragmentActivity.assertThatRunNavGraphCallbackOf<
        InequivalentlyRoutedDestinationActivity
      >()
      .isEqualTo(
        TestSingleFragmentActivity.NavGraphIntegrityCallback.INEQUIVALENT_DESTINATION_ROUTE
      )
  }

  @Test
  fun runsOnNonFragmentDestinationCallback() {
    TestSingleFragmentActivity.assertThatRunNavGraphCallbackOf<NonFragmentDestinationActivity>()
      .isEqualTo(TestSingleFragmentActivity.NavGraphIntegrityCallback.NON_FRAGMENT_DESTINATION)
  }

  @Test
  fun runsOnMultipleDestinationsCallback() {
    TestSingleFragmentActivity.assertThatRunNavGraphCallbackOf<MultipleDestinationsActivity>()
      .isEqualTo(TestSingleFragmentActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS)
  }

  @Test
  fun runsOnMultipleDestinationsCallbackWhenNavigatingToAnotherPosteriorlyAddedDestination() {
    TestSingleFragmentActivity.assertThatRunNavGraphCallbackOf<
        PosteriorlyAddedDestinationActivity
      >()
      .isEqualTo(TestSingleFragmentActivity.NavGraphIntegrityCallback.MULTIPLE_DESTINATIONS)
  }
}

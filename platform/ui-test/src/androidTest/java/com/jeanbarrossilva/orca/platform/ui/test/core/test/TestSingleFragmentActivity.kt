/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.test.core.test

import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals

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
     * Asserts that [expected] is the [NavGraphIntegrityCallback] to get run when this
     * [TestSingleFragmentActivity] is launched.
     *
     * @param expected [NavGraphIntegrityCallback] that's expected to be run.
     */
    inline fun <reified T : TestSingleFragmentActivity> assertRunNavGraphCallbackEquals(
      expected: NavGraphIntegrityCallback
    ) {
      var actual: NavGraphIntegrityCallback? = null
      launchActivity<T>().use { scenario ->
        scenario.onActivity { activity -> actual = activity.runNavGraphIntegrityCallback }
      }
      assertEquals(expected, actual)
    }
  }
}

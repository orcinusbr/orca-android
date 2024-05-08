/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.feature.registration

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.fragment.app.testing.launchFragmentInContainer
import assertk.assertThat
import br.com.orcinus.orca.platform.autos.test.kit.action.button.onPrimaryButton
import br.com.orcinus.orca.platform.navigation.navigator
import br.com.orcinus.orca.platform.navigation.test.activity.launchNavigationActivity
import br.com.orcinus.orca.platform.navigation.test.isAt
import br.com.orcinus.orca.std.injector.module.injection.injectionOf
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RegistrationFragmentTests {
  @get:Rule
  val injectorRule = InjectorTestRule {
    register(RegistrationModule(injectionOf { NoOpRegistrationBoundary }))
  }

  @Test
  fun navigates() {
    launchNavigationActivity().use { scenario ->
      scenario.onActivity { activity ->
        RegistrationFragment.navigate(activity.navigator)
        assertThat(activity).isAt<_, RegistrationFragment>()
      }
    }
  }

  @Test
  fun navigatesToCredentialsWhenContinuing() {
    @OptIn(ExperimentalTestApi::class)
    runComposeUiTest {
      mockkObject(NoOpRegistrationBoundary) {
        launchFragmentInContainer(instantiate = ::RegistrationFragment).use {
          onPrimaryButton().performClick()
          verify { NoOpRegistrationBoundary.navigateToCredentials() }
        }
      }
    }
  }
}

/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.registration

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.fragment.app.testing.launchFragmentInContainer
import com.jeanbarrossilva.orca.platform.autos.test.kit.action.button.onPrimaryButton
import com.jeanbarrossilva.orca.std.injector.module.injection.injectionOf
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.test.Test
import org.junit.Rule

internal class RegistrationFragmentTests {
  @get:Rule
  val injectorRule = InjectorTestRule {
    register(RegistrationModule(injectionOf { NoOpRegistrationBoundary }))
  }

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun navigatesToCredentialsWhenContinuing() {
    mockkObject(NoOpRegistrationBoundary) {
      launchFragmentInContainer(instantiate = ::RegistrationFragment).use {
        composeRule.onPrimaryButton().performClick()
        verify { NoOpRegistrationBoundary.navigateToCredentials() }
      }
    }
  }
}

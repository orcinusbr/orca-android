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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import br.com.orcinus.orca.platform.autos.i18n.ReadableThrowable
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.snack.onSnackbar
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.testing.context
import java.nio.channels.UnresolvedAddressException
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ErrorPresentationTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(UnsupportedOperationException::class)
  fun throwsWhenErrorIsNotCausedByAReadableException() {
    composeRule.setContent { ErrorPresentation(error = UnsupportedOperationException(Exception())) }
  }

  @Test
  fun showsSnackbarWhenErrorIsAnUnresolvedAddressException() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            val snackbarPresenter = rememberSnackbarPresenter()

            Scaffold(snackbarPresenter = snackbarPresenter) {
              ErrorPresentation(
                error = UnresolvedAddressException(),
                Modifier.padding(it),
                snackbarPresenter = snackbarPresenter
              )
            }
          }
        }
      }
      .onSnackbar()
      .assertIsDisplayed()
      .onChild()
      .assertTextEquals(ReadableThrowable.getDefault(context).localizedMessage)
  }

  @Test
  fun showsSnackbarWhenErrorIsCausedByAReadableException() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            val snackbarPresenter = rememberSnackbarPresenter()

            Scaffold(snackbarPresenter = snackbarPresenter) {
              ErrorPresentation(
                error = Throwable(ReadableThrowable.default),
                Modifier.padding(it),
                snackbarPresenter = snackbarPresenter
              )
            }
          }
        }
      }
      .onSnackbar()
      .assertIsDisplayed()
  }
}

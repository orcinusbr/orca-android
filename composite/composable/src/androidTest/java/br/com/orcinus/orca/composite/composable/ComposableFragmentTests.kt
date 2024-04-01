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

package br.com.orcinus.orca.composite.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runEmptyComposeUiTest
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import org.junit.Test

internal class ComposableFragmentTests {
  class TestComposableFragment : ComposableFragment() {
    @Composable @Suppress("TestFunctionName") override fun Content() {}
  }

  @Test
  fun setsComposableView() {
    @OptIn(ExperimentalTestApi::class)
    runEmptyComposeUiTest {
      launchFragmentInNavigationContainer(::TestComposableFragment).use { onRoot().assertExists() }
    }
  }
}

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

package com.jeanbarrossilva.orca.composite.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.navigation.NavGraphBuilder
import com.jeanbarrossilva.orca.platform.testing.activity.SingleFragmentActivity
import org.junit.Rule
import org.junit.Test

internal class ComposableFragmentTests {
  @get:Rule val composeRule = createAndroidComposeRule<TestComposableFragmentActivity>()

  class TestComposableFragmentActivity : SingleFragmentActivity() {
    override val route = "composable"

    override fun NavGraphBuilder.add() {
      fragment<TestComposableFragment>()
    }
  }

  class TestComposableFragment : ComposableFragment() {
    @Composable @Suppress("TestFunctionName") override fun Content() {}
  }

  @Test
  fun setsComposableView() {
    composeRule.onRoot().assertExists()
  }
}

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

package br.com.orcinus.orca.platform.autos.kit.sheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Dp
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import org.junit.Rule
import org.junit.Test

internal class WindowInsetsExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun takesFallbackWhenInsetsOneBoundIsUnspecified() {
    lateinit var insets: List<WindowInsets>
    composeRule.setContent {
      insets =
        remember {
            arrayOf(
              WindowInsets(left = Dp.Unspecified),
              WindowInsets(top = Dp.Unspecified),
              WindowInsets(right = Dp.Unspecified),
              WindowInsets(bottom = Dp.Unspecified)
            )
          }
          .map { it.takeOrElse { WindowInsets.Zero } }
    }
    assertThat(insets).each { it.isEqualTo(WindowInsets.Zero) }
  }
}

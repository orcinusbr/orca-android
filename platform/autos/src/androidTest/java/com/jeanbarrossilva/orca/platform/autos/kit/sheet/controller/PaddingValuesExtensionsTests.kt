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

package com.jeanbarrossilva.orca.platform.autos.kit.sheet.controller

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.plus
import org.junit.Rule
import org.junit.Test

internal class PaddingValuesExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun sumsWithDPs() {
    composeRule.setContent {
      (PaddingValues(start = 2.dp, top = 4.dp, end = 16.dp, bottom = 256.dp) + 2.dp).let {
        DisposableEffect(Unit) {
          assertThat(it)
            .isEqualTo(PaddingValues(start = 4.dp, top = 6.dp, end = 18.dp, bottom = 258.dp))
          onDispose {}
        }
      }
    }
  }
}

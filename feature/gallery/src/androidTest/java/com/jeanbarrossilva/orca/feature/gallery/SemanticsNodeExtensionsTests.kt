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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.feature.gallery.test.isDisplayed
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun nodeIsDisplayed() {
    assertThat(
        composeRule
          .apply { setContent { Spacer(Modifier.size(1.dp)) } }
          .onRoot()
          .fetchSemanticsNode()
          .isDisplayed
      )
      .isTrue()
  }

  @Test
  fun nodeIsNotDisplayed() {
    assertThat(
        composeRule
          .apply { setContent { Spacer(Modifier) } }
          .onRoot()
          .fetchSemanticsNode()
          .isDisplayed
      )
      .isFalse()
  }
}

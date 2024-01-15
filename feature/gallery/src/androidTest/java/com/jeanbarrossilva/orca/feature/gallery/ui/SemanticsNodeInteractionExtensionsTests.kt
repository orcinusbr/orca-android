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

package com.jeanbarrossilva.orca.feature.gallery.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.feature.gallery.ui.test.get
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun getsWhetherMatcherMatchesNodeOfInteractionWhenItDoes() {
    assertThat(composeRule.apply { setContent {} }.onRoot()[isRoot()]).isTrue()
  }

  @Test
  fun getsWhetherMatcherMatchesNodeOfInteractionWhenItDoesNot() {
    assertThat(
        composeRule.apply { setContent { Text("🐋") } }.onNodeWithText("🐋")[hasTextExactly("🦈")]
      )
      .isFalse()
  }
}

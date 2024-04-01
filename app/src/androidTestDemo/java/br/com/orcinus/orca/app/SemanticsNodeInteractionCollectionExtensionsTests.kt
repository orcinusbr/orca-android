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

package br.com.orcinus.orca.app

import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.app.demo.test.perform
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionCollectionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun performsActionOnEachNode() {
    val texts = arrayOfNulls<String>(size = 2)
    composeRule
      .apply {
        setContent {
          Text("0")
          Text("1")
        }
      }
      .perform({ onAllNodes(hasTextExactly("0") or hasTextExactly("1")) }) {
        texts[it] = fetchSemanticsNode().config[SemanticsProperties.Text].single().toString()
      }
    assertThat(texts).containsExactly("0", "1")
  }

  @Test(expected = AssertionError::class)
  fun throwsAssertionErrorThrownByActionPerformedOnEachNode() {
    composeRule
      .apply {
        setContent {
          Text("2")
          Text("3")
        }
      }
      .perform({ onAllNodes(hasTextExactly("2") or hasTextExactly("3")) }) {
        assertIsNotDisplayed()
      }
  }
}

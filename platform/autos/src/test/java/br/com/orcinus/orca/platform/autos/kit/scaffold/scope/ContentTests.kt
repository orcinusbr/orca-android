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

package br.com.orcinus.orca.platform.autos.kit.scaffold.scope

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onRoot
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ContentTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun expandedValueIsModified() {
    composeRule
      .apply { setContent { Content.Expanded(Modifier.testTag("content")) {}.Value() } }
      .onRoot()
      .onChild()
      .assert(hasTestTag("content"))
  }

  @Test
  fun navigableValueIsModified() {
    composeRule
      .apply { setContent { Content.Expanded(Modifier.testTag("content")) {}.Value() } }
      .onRoot()
      .onChild()
      .assert(hasTestTag("content"))
  }
}

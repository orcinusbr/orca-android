/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.jeanbarrossilva.orca.feature.composer.test.assertTextEquals
import com.jeanbarrossilva.orca.feature.composer.test.isBoldFormat
import com.jeanbarrossilva.orca.feature.composer.test.isItalicFormat
import com.jeanbarrossilva.orca.feature.composer.test.onToolbar
import com.jeanbarrossilva.orca.platform.autos.test.kit.input.text.onTextField
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
internal class ComposerActivityTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComposerActivity>()

  @Test
  fun stylesSelectedComposition() {
    composeRule.onTextField().performTextInput("Hello, world!")
    composeRule.onTextField().performTextInputSelection(TextRange(0, 5))
    composeRule.onToolbar().onChildren().filterToOne(isBoldFormat()).performClick()
    composeRule
      .onTextField()
      .assertTextEquals(
        buildAnnotatedString {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
          append(", world!")
        }
      )
  }

  @Ignore(
    "androidx.compose.ui:ui-text:1.5.0 has a bug that prevents stylization from being kept " +
      "across selection changes."
  )
  @Test
  fun keepsStylizationWhenUnselectingStylizedComposition() {
    composeRule.onTextField().performTextInput("Hello, world!")
    composeRule.onTextField().performTextInputSelection(TextRange(7, 12))
    composeRule.onToolbar().onChildren().filterToOne(isItalicFormat()).performClick()
    repeat(64) { composeRule.onTextField().performTextInputSelection(TextRange((0..12).random())) }
    composeRule
      .onTextField()
      .assertTextEquals(
        buildAnnotatedString {
          append("Hello, ")
          withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("world") }
          append('!')
        }
      )
  }
}

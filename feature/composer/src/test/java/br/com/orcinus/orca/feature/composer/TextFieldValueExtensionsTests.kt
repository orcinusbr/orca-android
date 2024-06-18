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

package br.com.orcinus.orca.feature.composer

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class TextFieldValueExtensionsTests {
  @Test
  fun `GIVEN a value with a bold portion WHEN selecting it and checking if it's bold THEN it is`() {
    assertTrue(
      TextFieldValue(
          buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
            append(", world!")
          },
          selection = TextRange(0, 5)
        )
        .isSelectionBold
    )
  }

  @Test
  fun `GIVEN a value without a bold portion WHEN selecting and toggling it THEN it is bold`() {
    assertEquals(
      TextFieldValue(
        buildAnnotatedString {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
          append(", world!")
        },
        selection = TextRange(0, 5)
      ),
      TextFieldValue(AnnotatedString("Hello, world!"), selection = TextRange(0, 5))
        .withBoldSelection(true)
    )
  }

  @Test
  fun `GIVEN a value with a bold portion WHEN selecting and toggling it THEN it isn't bold`() {
    assertEquals(
      TextFieldValue(
        buildAnnotatedString {
          withStyle(SpanStyle()) { append("Hello") }
          append(", world!")
        },
        selection = TextRange(0, 5)
      ),
      TextFieldValue(
          buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
            append(", world!")
          },
          selection = TextRange(0, 5)
        )
        .withBoldSelection(false)
    )
  }
}

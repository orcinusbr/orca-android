/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.feature.composer

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

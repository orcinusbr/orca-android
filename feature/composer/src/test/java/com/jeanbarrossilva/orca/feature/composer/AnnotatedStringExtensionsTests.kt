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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AnnotatedStringExtensionsTests {
  @Test
  fun `GIVEN an AnnotatedString with an un-styled portion WHEN getting its span styles THEN it's an empty list`() {
    assertEquals(
      emptyList<AnnotatedString.Range<SpanStyle>>(),
      buildAnnotatedString {
          append("Hello, ")
          withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("world") }
        }
        .getSpanStylesWithin(0..5)
    )
  }

  @Test
  fun `GIVEN an AnnotatedString with a portion of it styled WHEN getting its span styles THEN it's equivalent to its styling`() {
    assertEquals(
      listOf(
        AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = 5),
        AnnotatedString.Range(SpanStyle(Color.Blue), start = 12, end = 13)
      ),
      buildAnnotatedString {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
          append(", world")
          withStyle(SpanStyle(Color.Blue)) { append('!') }
        }
        .getSpanStylesWithin(0..13)
    )
  }

  @Test
  fun `GIVEN an AnnotatedString without span styles WHEN replacing the ones within a range THEN the replacement is added`() {
    assertEquals(
      buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
        append(", world!")
      },
      AnnotatedString("Hello, world!").replacingSpanStylesWithin(0..5) {
        copy(fontWeight = FontWeight.Bold)
      }
    )
  }
}

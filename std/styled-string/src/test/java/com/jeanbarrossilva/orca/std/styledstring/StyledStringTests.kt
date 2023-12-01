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

package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import com.jeanbarrossilva.orca.std.styledstring.style.type.test.url
import com.jeanbarrossilva.orca.std.styledstring.style.type.test.username
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class StyledStringTests {
  @Test
  fun `GIVEN an invalid e-mail WHEN appending it THEN it isn't stylized as an e-mail`() {
    assertContentEquals(buildStyledString { +"john@@appleseed.com" }.styles, emptyList())
  }

  @Test
  fun `GIVEN an invalid subject WHEN appending a hashtag THEN it throws`() {
    assertFailsWith<Style.Constrained.InvalidTargetException> {
      buildStyledString { hashtag { +"subjects - cannot - have - whitespaces" } }
    }
  }

  @Test
  fun `GIVEN a mention WHEN appending it THEN it's been placed correctly`() {
    assertEquals(
      Mention(indices = 7..(7 + Mention.username.lastIndex), Mention.url),
      buildStyledString {
          +"Hello, "
          mention(Mention.url) { +Mention.username }
          +"!"
        }
        .also(::println)
        .styles
        .single()
    )
  }

  @Test
  fun `GIVEN nested styles WHEN appending text with them THEN they've been applied`() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(0..4)),
      buildStyledString {
          bold { italic { +"Hello" } }
          +'!'
        }
        .styles
    )
  }

  @Test
  fun `GIVEN a styled string WHEN copying it with chopped styles THEN its styles are adapted`() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(6..7)),
      buildStyledString {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(8) }
        .styles
    )
  }

  @Test
  fun `GIVEN a styled string WHEN copying it with dropped styles THEN its styles are adapted`() {
    assertContentEquals(
      listOf(Bold(0..4)),
      buildStyledString {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(5) }
        .styles
    )
  }
}

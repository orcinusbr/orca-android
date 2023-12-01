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

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import java.net.URL
import org.junit.Test

internal class AnyExtensionsTests {
  @Test
  fun structurallyComparesEqualStyleSpans() {
    assertThat(
        (StyleSpan(Typeface.NORMAL) as Any).isStructurallyEqualTo(StyleSpan(Typeface.NORMAL))
      )
      .isTrue()
  }

  @Test
  fun structurallyComparesDifferentStyleSpans() {
    assertThat((StyleSpan(Typeface.NORMAL) as Any).isStructurallyEqualTo(StyleSpan(Typeface.BOLD)))
      .isFalse()
  }

  @Test
  fun structurallyComparesEqualURLSpans() {
    assertThat(
        URLSpan("https://orca.jeanbarrossilva.com")
          .isStructurallyEqualTo(URLSpan("https://orca.jeanbarrossilva.com"))
      )
      .isTrue()
  }

  @Test
  fun structurallyComparesDifferentURLSpans() {
    assertThat(
        URLSpan("https://orca.jeanbarrossilva.com")
          .isStructurallyEqualTo(URLSpan("https://beluga.jeanbarrossilva.com"))
      )
      .isFalse()
  }

  @Test
  fun convertsBoldStyleSpanIntoStyle() {
    assertThat((StyleSpan(Typeface.BOLD) as Any).toStyles(0..8)).containsExactly(Bold(0..8))
  }

  @Test
  fun convertsBoldItalicStyleSpanIntoStyles() {
    assertThat((StyleSpan(Typeface.BOLD_ITALIC) as Any).toStyles(0..8))
      .containsExactly(Bold(0..8), Italic(0..8))
  }

  @Test
  fun convertsItalicStyleSpanIntoStyle() {
    assertThat((StyleSpan(Typeface.ITALIC) as Any).toStyles(0..8)).containsExactly(Italic(0..8))
  }

  @Test
  fun convertsURLSpanIntoStyle() {
    assertThat(URLSpan("https://orca.jeanbarrossilva.com").toStyles(0..31))
      .containsExactly(Link.to(URL("https://orca.jeanbarrossilva.com"), 0..31))
  }
}

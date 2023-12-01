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
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import org.junit.Test

internal class SpannedPartTests {
  @Test
  fun comparesEqualSpannedParts() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isEqualTo(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInIndices() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(Part(1..9).span(StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInSpans() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(Part(0..8).span(StyleSpan(Typeface.BOLD)))
  }

  @Test
  fun spans() {
    val span = StyleSpan(Typeface.NORMAL)
    assertThat(Part(0..8).span(span)).all {
      prop(Part.Spanned::getIndices).isEqualTo(0..8)
      prop(Part.Spanned::getSpans).isEqualTo(listOf(span))
    }
  }
}

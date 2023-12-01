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
import android.text.Html
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.Test

internal class SpannedExtensionsTests {
  @Test
  fun partitions() {
    assertThat(
        Html.fromHtml("<p><b><i>Hello</i></b>, <i>world</i>!</p>", Html.FROM_HTML_MODE_COMPACT)
          .parts
      )
      .containsExactly(
        Part(0..4).span(StyleSpan(Typeface.ITALIC)),
        Part(0..4).span(StyleSpan(Typeface.BOLD, fontWeightAdjustment = 0)),
        Part(5..6),
        Part(7..11).span(StyleSpan(Typeface.ITALIC))
      )
  }
}

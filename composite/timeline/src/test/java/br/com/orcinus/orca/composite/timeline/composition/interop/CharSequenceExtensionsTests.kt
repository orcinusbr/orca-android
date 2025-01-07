/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CharSequenceExtensionsTests {
  @Test
  fun convertsIntoMarkdown() {
    assertThat("Hello, world!".toMarkdown(context)).isEqualTo(Markdown.unstyled("Hello, world!"))
  }

  @Test
  fun returnsReceiverMarkdownWhenConvertingItIntoOne() {
    val markdown = Markdown.unstyled("Hello, world!")
    assertThat(markdown.toMarkdown(context)).isSameInstanceAs(markdown)
  }

  @Test
  fun convertsSpannedIntoMarkdown() {
    assertThat(
        SpannableStringBuilder()
          .append("Hello", StyleSpan(Typeface.BOLD), 0)
          .append(", ")
          .append("world", StyleSpan(Typeface.ITALIC), 0)
          .append('!')
          .let { it as CharSequence }
          .toMarkdown(context)
      )
      .isEqualTo(
        buildMarkdown {
          bold { +"Hello" }
          +", "
          italic { +"world" }
          +'!'
        }
      )
  }
}

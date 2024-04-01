/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.text.annotated.fromHtml
import br.com.orcinus.orca.std.styledstring.StyledString
import org.junit.Test

internal class StyledStringExtensionsTests {
  @Test
  fun breaksLineTwiceBetweenParagraphsWhenConvertingHtmlToStyledString() {
    assertThat(StyledString.fromHtml("<p>👔</p><p>🥾</p>")).isEqualTo(StyledString("👔\n\n🥾"))
  }
}

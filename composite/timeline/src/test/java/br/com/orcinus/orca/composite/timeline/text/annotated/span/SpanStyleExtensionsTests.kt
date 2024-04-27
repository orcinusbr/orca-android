/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.text.annotated.span

import androidx.compose.ui.text.SpanStyle
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.std.uri.URIBuilder
import kotlin.test.Test

internal class SpanStyleExtensionsTests {
  @Test(expected = IllegalStateException::class)
  fun throwsWhenGettingURIFromSpanStyleWithoutFontFeatureSettings() {
    SpanStyle().uri
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenGettingURIFromSpanStyleWithFontFeatureSettingsWithoutCategory() {
    SpanStyle(fontFeatureSettings = "").uri
  }

  @Test
  fun getsURIFromSpanStyle() {
    val uri = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").build()
    assertThat(createLinkSpanStyle(Colors.LIGHT, uri).uri).isEqualTo(uri)
  }
}

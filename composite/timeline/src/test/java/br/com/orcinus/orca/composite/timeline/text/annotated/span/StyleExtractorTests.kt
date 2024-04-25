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
import assertk.assertions.isNull
import assertk.assertions.isTrue
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.text.annotated.span.category.Categorizer
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.MalformedURLException
import java.net.URL
import kotlin.test.Test

internal class StyleExtractorTests {
  @Test
  fun boldExtractorDoesNotExtractFromNonBoldSpanStyle() {
    assertThat(StyleExtractor.BOLD.extract(SpanStyle(), 0..1)).isNull()
  }

  @Test
  fun boldExtractorExtractsFromSpanStyle() {
    assertThat(StyleExtractor.BOLD.extract(BoldSpanStyle, 0..1)).isEqualTo(Style.Bold(0..1))
  }

  @Test
  fun emailExtractorDoesNotExtractFromNonEmailSpanStyle() {
    assertThat(StyleExtractor.EMAIL.extract(SpanStyle(), 0..1)).isNull()
  }

  @Test
  fun emailExtractorExtractsFromSpanStyle() {
    assertThat(
        StyleExtractor.EMAIL.isExtractable(
          createLinkSpanStyle(Colors.LIGHT, Categorizer.categorizeAsEmail())
        )
      )
      .isTrue()
  }

  @Test
  fun hashtagExtractorDoesNotExtractFromNonHashtagSpanStyle() {
    assertThat(StyleExtractor.HASHTAG.extract(SpanStyle(), 0..1)).isNull()
  }

  @Test
  fun hashtagExtractorExtractsFromSpanStyle() {
    assertThat(
        StyleExtractor.HASHTAG.isExtractable(
          createLinkSpanStyle(Colors.LIGHT, Categorizer.categorizeAsHashtag())
        )
      )
      .isTrue()
  }

  @Test
  fun italicExtractorDoesNotExtractFromNonItalicSpanStyle() {
    assertThat(StyleExtractor.ITALIC.extract(SpanStyle(), 0..1)).isNull()
  }

  @Test
  fun italicExtractorExtractsFromSpanStyle() {
    assertThat(StyleExtractor.ITALIC.extract(ItalicSpanStyle, 0..1)).isEqualTo(Style.Italic(0..1))
  }

  @Test
  fun linkExtractorDoesNotExtractFromNonURLSpanStyle() {
    assertThat(StyleExtractor.LINK.extract(SpanStyle(), 0..1))
  }

  @Test(expected = MalformedURLException::class)
  fun linkExtractorThrowsWhenExtractingFromSpanStyleWithMalformedCallToUrl() {
    StyleExtractor.LINK.extract(createLinkSpanStyle(Colors.LIGHT, "category: url("), 0..1)
  }

  @Test(expected = MalformedURLException::class)
  fun linkExtractorThrowsWhenExtractingFromSpanStyleWithMalformedURL() {
    StyleExtractor.LINK.extract(createLinkSpanStyle(Colors.LIGHT, "category: url()"), 0..1)
  }

  @Test
  fun linkExtractorExtractsFromSpanStyle() {
    assertThat(
        StyleExtractor.LINK.extract(
          createLinkSpanStyle(
            Colors.LIGHT,
            Categorizer.categorizeAsLink(URL("https://orca.jeanbarrossilva.com"))
          ),
          0..1
        )
      )
      .isEqualTo(Style.Link.to(URL("https://orca.jeanbarrossilva.com"), 0..1))
  }

  @Test
  fun mentionExtractorDoesNotExtractFromNonMentionSpanStyle() {
    assertThat(StyleExtractor.MENTION.extract(SpanStyle(), 0..1)).isNull()
  }

  @Test
  fun mentionExtractorExtractsFromSpanStyle() {
    assertThat(
        StyleExtractor.MENTION.isExtractable(
          createLinkSpanStyle(
            Colors.LIGHT,
            Categorizer.categorizeAsMention(URL("https://orca.jeanbarrossilva.com"))
          )
        )
      )
      .isTrue()
  }
}

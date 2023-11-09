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

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.Test

internal class PartTests {
  @Test
  fun spans() {
    val span = StyleSpan(Typeface.NORMAL)
    assertThat(Part(0..8).span(span)).all {
      prop(Part.Spanned::getIndices).isEqualTo(0..8)
      prop(Part.Spanned::getSpans).isEqualTo(listOf(span))
    }
  }
}

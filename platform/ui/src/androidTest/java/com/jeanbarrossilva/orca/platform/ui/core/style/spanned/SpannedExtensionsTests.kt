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

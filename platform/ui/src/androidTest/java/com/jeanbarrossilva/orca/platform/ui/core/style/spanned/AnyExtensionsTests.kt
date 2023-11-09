package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Test

internal class AnyExtensionsTests {
  @Test
  fun structurallyComparesEqualStyleSpans() {
    assertThat(StyleSpan(Typeface.NORMAL).isStructurallyEqualTo(StyleSpan(Typeface.NORMAL)))
      .isTrue()
  }

  @Test
  fun structurallyComparesDifferentStyleSpans() {
    assertThat(StyleSpan(Typeface.NORMAL).isStructurallyEqualTo(StyleSpan(Typeface.BOLD))).isFalse()
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
}

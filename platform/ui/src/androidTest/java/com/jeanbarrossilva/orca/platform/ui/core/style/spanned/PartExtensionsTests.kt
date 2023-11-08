package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

internal class PartExtensionsTests {
  @Test
  fun createsSpannedPart() {
    val span = StyleSpan(Typeface.NORMAL)
    assertThat(Part(0..8) + span).isEqualTo(Part.Spanned(0..8, span))
  }
}

package com.jeanbarrossilva.orca.platform.ui.core.style

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import org.junit.Test

internal class StyledStringExtensionsTests {
  @Test
  fun breaksLineTwiceBetweenParagraphsWhenConvertingHtmlToStyledString() {
    assertThat(StyledString.fromHtml("<p>👔</p><p>🥾</p>")).isEqualTo(StyledString("👔\n\n🥾"))
  }
}

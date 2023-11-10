package com.jeanbarrossilva.orca.std.styledstring.style

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import kotlin.test.Test

internal class StyleExtensionsTests {
  @Test
  fun isWithin() {
    assertThat(Bold(0..4).isWithin("Hello!"))
  }

  @Test
  fun isNotWithin() {
    assertThat(Bold(0..6).isWithin("Hello."))
  }

  @Test
  fun isChopped() {
    assertThat(Bold(0..Int.MAX_VALUE).isChoppedBy("🦩")).isTrue()
  }

  @Test
  fun isNotChopped() {
    assertThat(Bold(0..4).isChoppedBy("Hello, world!")).isFalse()
  }
}

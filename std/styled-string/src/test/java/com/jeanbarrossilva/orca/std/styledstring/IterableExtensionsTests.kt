package com.jeanbarrossilva.orca.std.styledstring

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.test.Test

internal class IterableExtensionsTests {
  @Test
  fun mapsConditionally() {
    assertThat(listOf(0, 1, 2, 3, 4).map({ it % 2 == 0 }) { it * it })
      .containsExactly(0, 1, 4, 3, 16)
  }
}

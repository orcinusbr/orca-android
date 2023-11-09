package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.Test

internal class IterableExtensionsTests {
  @Test
  fun returnsEmptyListWhenConsecutivizingEmptyIterableOfInts() {
    assertThat(emptyList<Int>().consecutively()).isEmpty()
  }

  @Test
  fun consecutivizes() {
    assertThat(listOf(0, 1, 2, 4, 5, 8).consecutively()).containsExactly(0..2, 4..5, 8..8)
  }

  @Test
  fun sortsByFirstValuesAscendinglyWhenConsecutivizing() {
    assertThat(listOf(1, 2, 5, 6, 3, 4).consecutively()).containsExactly(1..2, 3..4, 5..6)
  }
}

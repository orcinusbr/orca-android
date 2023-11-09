package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.Test

internal class IterableExtensionsTests {
  @Test
  fun returnsEmptyListWhenGroupingEmptyIterableOfIntsByConsecutiveness() {
    assertThat(emptyList<Int>().groupByConsecutiveness()).isEmpty()
  }

  @Test
  fun groupsIntsByConsecutiveness() {
    assertThat(listOf(0, 1, 2, 4, 5, 8).groupByConsecutiveness()).containsExactly(0..2, 4..5, 8..8)
  }

  @Test
  fun sortsFromLesserToGreaterFirstValuesWhenGroupingIntsByConsecutiveness() {
    assertThat(listOf(1, 2, 5, 6, 3, 4).groupByConsecutiveness()).containsExactly(1..2, 3..4, 5..6)
  }
}

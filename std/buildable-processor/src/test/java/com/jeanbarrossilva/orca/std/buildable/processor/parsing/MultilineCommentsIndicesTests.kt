package com.jeanbarrossilva.orca.std.buildable.processor.parsing

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class MultilineCommentsIndicesTests {
  @Test
  fun extracts() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", "* 😛", "*/"))).containsExactly(0..2)
  }

  @Test
  fun extractsNothingWhenThereAreNoMultilineComments() {
    assertThat(MultilineCommentsIndices.of(arrayOf("var index = 0", "index++")))
      .isEqualTo(emptyArray())
  }

  @Test
  fun extractsNothingWhenOpeningDelimiterContainsNoMatchingClosingOne() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", "* 🐙"))).isEqualTo(emptyArray())
  }

  @Test(expected = MultilineCommentsIndices.NonDelimitedBodyException::class)
  fun throwsWhenBodyContainsNoDelimiter() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", "🧠", "*/"))).isEqualTo(emptyArray())
  }

  @Test
  fun extractsWithBodyContainingLeadingWhitespaces() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", " * Hello,", "  * world!", "*/")))
      .containsExactly(0..3)
  }

  @Test
  fun extractsWithBodyContainingMultipleLeadingDelimiters() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", "** H", "*** i", "**** !", "*/")))
      .containsExactly(0..4)
  }

  @Test
  fun extractWithBodyContainingMultipleLeadingWhitespacesAndDelimiters() {
    assertThat(MultilineCommentsIndices.of(arrayOf("/**", " ** 😴", "  **** 🫨", "*/")))
      .containsExactly(0..3)
  }
}

package com.jeanbarrossilva.orca.platform.ui.core.replacement

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ReplacementListTests {
  @Test
  fun adds() {
    assertEquals(replacementListOf(0), emptyReplacementList<Int>().apply { add(0) })
  }

  @Test
  fun replaces() {
    assertEquals(
      replacementListOf("Hello,", "world!", selector = String::first),
      replacementListOf("Hey,", "world!", selector = String::first).apply { add("Hello,") }
    )
  }
}

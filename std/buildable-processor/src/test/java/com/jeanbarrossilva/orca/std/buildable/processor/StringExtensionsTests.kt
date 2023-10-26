package com.jeanbarrossilva.orca.std.buildable.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class StringExtensionsTests {
  @Test
  fun replacesLast() {
    assertThat(":P :| :P".replaceLast(":P", ":p")).isEqualTo(":P :| :p")
  }
}

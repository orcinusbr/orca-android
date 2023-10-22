package com.jeanbarrossilva.orca.std.buildable.processor.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.buildable.processor.grammar.PossessiveApostrophe
import kotlin.test.Test

internal class PossessiveApostropheTests {
  @Test
  fun hasTrailingSWhenStringIsEmpty() {
    assertThat(PossessiveApostrophe.of("")).isEqualTo(
        PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun hasTrailingSWhenStringIsBlank() {
    assertThat(PossessiveApostrophe.of(" ")).isEqualTo(
        PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun hasTrailingSWhenStringDoesNotEndWithS() {
    assertThat(PossessiveApostrophe.of("Jean")).isEqualTo(
        PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun doesNotHaveTrailingSWhenStringEndsWithS() {
    assertThat(PossessiveApostrophe.of("jeans")).isEqualTo(
        PossessiveApostrophe.WITHOUT_TRAILING_S)
  }
}

package com.jeanbarrossilva.orca.std.buildable.processor.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class IndefiniteArticleTests {
  @Test
  fun aPrecedesEmptyString() {
    assertThat(IndefiniteArticle.of(" ")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesAmpersandLiteral() {
    assertThat(IndefiniteArticle.of("&")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesSemiColonLiteral() {
    assertThat(IndefiniteArticle.of(";")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesWordsStartingWithNonUVowels() {
    assertThat(IndefiniteArticle.of("asterisk")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("elevator")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("indicate")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("outdated")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun anPrecedesWordUncharted() {
    assertThat(IndefiniteArticle.of("uncharted")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesWordUniversity() {
    assertThat(IndefiniteArticle.of("university")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesNflAcronym() {
    assertThat(IndefiniteArticle.of("NFL")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesDigitOne() {
    assertThat(IndefiniteArticle.of("1")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun aPrecedesWordOne() {
    assertThat(IndefiniteArticle.of("one")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesDigitEight() {
    assertThat(IndefiniteArticle.of("8")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun anPrecedesWordEight() {
    assertThat(IndefiniteArticle.of("eight")).isEqualTo(IndefiniteArticle.AN)
  }
}

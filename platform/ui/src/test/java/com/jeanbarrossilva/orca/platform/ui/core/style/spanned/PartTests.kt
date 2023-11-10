package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.Test

internal class PartTests {
  @Test
  fun comparesEqualParts() {
    assertThat(Part(0..8)).isEqualTo(Part(0..8))
  }

  @Test
  fun comparesDifferentParts() {
    assertThat(Part(0..8)).isNotEqualTo(Part(1..9))
  }
}

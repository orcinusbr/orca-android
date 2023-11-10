package com.jeanbarrossilva.orca.core.instance.domain

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import kotlin.test.Test

internal class DomainTests {
  @Test
  fun urlProtocolIsHttps() {
    assertThat(Domain.sample.url.protocol).isEqualTo("https")
  }

  @Test
  fun urlHostIsDomain() {
    assertThat(Domain.sample.url.host).isEqualTo("${Domain.sample}")
  }

  @Test
  fun urlDoesNotHavePath() {
    assertThat(Domain.sample.url.path).isEmpty()
  }
}

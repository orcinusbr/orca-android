package com.jeanbarrossilva.orca.core.instance.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import java.net.URL
import kotlin.test.Test

internal class URLExtensionsTests {
  @Test
  fun isInternalResourceURL() {
    assertThat(URL(Domain.sample.url, "path").isOfResourceFrom(Domain.sample)).isTrue()
  }

  @Test
  fun isExternalResourceURL() {
    assertThat(URL("https", "google.com", " ").isOfResourceFrom(Domain.sample)).isFalse()
  }
}

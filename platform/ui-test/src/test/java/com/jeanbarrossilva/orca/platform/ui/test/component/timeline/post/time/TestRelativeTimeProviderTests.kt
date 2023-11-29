package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test

internal class TestRelativeTimeProviderTests {
  @Test
  fun providesStringRepresentation() {
    val dateTime = ZonedDateTime.of(2003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))
    assertThat(TestRelativeTimeProvider.onProvide(dateTime)).isEqualTo("$dateTime")
  }
}

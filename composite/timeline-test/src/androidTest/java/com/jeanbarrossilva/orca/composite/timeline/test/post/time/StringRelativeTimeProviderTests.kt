/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.test.post.time

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Test

internal class StringRelativeTimeProviderTests {
  @Test
  fun providesStringRepresentation() {
    val dateTime = ZonedDateTime.of(2003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))
    assertThat(StringRelativeTimeProvider.onProvide(dateTime)).isEqualTo("$dateTime")
  }
}

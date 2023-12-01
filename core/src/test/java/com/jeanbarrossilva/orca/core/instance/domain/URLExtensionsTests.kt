/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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

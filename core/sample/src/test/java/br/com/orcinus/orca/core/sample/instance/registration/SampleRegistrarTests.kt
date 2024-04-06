/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.instance.registration

import assertk.assertThat
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.sample.instance.domain.sample
import kotlin.test.Test
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest

internal class SampleRegistrarTests {
  @Test
  fun registrationSucceedsInSampleInstance() {
    runTest {
      assertThat(
          SampleRegistrar.register(Credentials.sample.email, Credentials.sample.password)
            .filter { it.domain == Domain.sample }
            .single()
            .hasSucceeded
        )
        .isTrue()
    }
  }
}

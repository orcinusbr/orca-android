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

package br.com.orcinus.orca.core.instance.registration

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.instance.domain.samples
import br.com.orcinus.orca.core.sample.instance.registration.sample
import kotlin.test.Test
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest

internal class RegistrarTests {
  @Test
  fun emitsFailedRegistrationWhenInstancesAreUnavailable() {
    runTest {
      object : Registrar() {
          override val domains = listOf(Domain.sample)

          override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
            return false
          }
        }
        .register(Credentials.sample.email, Credentials.sample.password)
        .test {
          assertThat(awaitItem().hasSucceeded).isFalse()
          awaitComplete()
        }
    }
  }

  @Test
  fun emitsSuccessfulRegistrationWhenAnInstanceIsAvailable() {
    runTest {
      object : Registrar() {
          override val domains = Domain.samples.take(2)

          override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
            return domain == domains.last()
          }
        }
        .register(Credentials.sample.email, Credentials.sample.password)
        .test {
          assertThat(awaitItem().hasSucceeded).isFalse()
          assertThat(awaitItem().hasSucceeded).isTrue()
          awaitComplete()
        }
    }
  }

  @Test
  fun attemptsToRegisterAtEachInstanceWhenNoneIsAvailable() {
    runTest {
      object : Registrar() {
          override val domains = Domain.samples

          override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
            return false
          }
        }
        .register(Credentials.sample.email, Credentials.sample.password)
        .test {
          Domain.samples.forEach { assertThat(awaitItem().domain).isEqualTo(it) }
          awaitComplete()
        }
    }
  }

  @Test
  fun doesNotPerformRegistrationAttemptsAfterSuccessfulOne() {
    var attemptCount = 0
    runTest {
      object : Registrar() {
          override val domains = Domain.samples.take(2)

          override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
            attemptCount++
            return domain == domains.first()
          }
        }
        .register(Credentials.sample.email, Credentials.sample.password)
        .collect()
    }
    assertThat(attemptCount).isEqualTo(1)
  }
}

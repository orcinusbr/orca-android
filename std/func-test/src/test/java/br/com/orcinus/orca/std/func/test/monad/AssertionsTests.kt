/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.std.func.test.monad

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.std.func.monad.Maybe
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertionsTests {
  private val exception = Exception()

  @Test
  fun failsWhenAssertingThatFailedMaybeIsSuccessful() {
    assertFailure { assertThat(Maybe).failed<_, Any>(exception).isSuccessful() }
      .isInstanceOf<AssertionFailedError>()
  }

  @Test
  fun passesWhenAssertingThatSuccessfulMaybeIsSuccessful() {
    assertThat(Maybe).successful<Exception, _>(0).isSuccessful()
  }

  @Test
  fun failsWhenAssertingThatSuccessfulMaybeIsFailed() {
    assertFailure { assertThat(Maybe).successful<Exception, _>(0).isFailed() }
      .isInstanceOf<AssertionFailedError>()
  }

  @Test
  fun passesWhenAssertingThatFailedMaybeIsFailed() {
    assertThat(Maybe).failed<_, Any>(exception).isFailed()
  }
}

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

import assertk.assertThat
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isZero
import br.com.orcinus.orca.std.func.monad.Maybe
import kotlin.test.Test

internal class FactoriesTests {
  @Test
  fun createsAssertionForASuccessfulMaybe() =
    assertThat(Maybe).successful<Exception, _>(0).isSuccessful().isZero()

  @Test
  fun createsAssertionForAFailedMaybe() {
    val exception = Exception()
    assertThat(Maybe).failed<_, Any>(exception).isFailed().isSameInstanceAs(exception)
  }
}

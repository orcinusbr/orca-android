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

package br.com.orcinus.orca.ext.coroutines.notifier

import assertk.assertThat
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSameAs
import br.com.orcinus.orca.ext.reflection.access
import kotlin.test.Test

internal class NotifierTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingSuccessorOfUnknownNotifier() {
    Notifier::class.constructors[String::class].access { call("Notifier.unknown") }.next()
  }

  @Test
  fun subsequentIsSuccessorOfInitial() {
    assertThat(Notifier.initial.next()).isNotEqualTo(Notifier.initial)
  }

  @Test
  fun initialIsSuccessorOfSubsequent() {
    assertThat(Notifier.initial.next().next()).isSameAs(Notifier.initial)
  }
}

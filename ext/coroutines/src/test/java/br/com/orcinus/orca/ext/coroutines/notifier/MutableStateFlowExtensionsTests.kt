/*
 * Copyright © 2023–2024 Orcinus
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

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isSameAs
import kotlin.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

internal class MutableStateFlowExtensionsTests {
  @Test
  fun notifierFlowHasInitialNotifierAsItsInitialValue() {
    assertThat(notifierFlow().value).isSameAs(Notifier.initial)
  }

  @Test
  fun notifierFlowReceivesSubsequentNotifierWhenNotifiedWithInitialAsItsValue() {
    val flow = notifierFlow()
    runTest {
      flow.test {
        awaitItem()
        flow.notify()
        assertThat(awaitItem()).isSameAs(Notifier.initial.next())
      }
    }
  }

  @Test
  fun notifierFlowReceivesInitialNotifierWhenNotifiedWithSubsequentAsItsValue() {
    val flow = notifierFlow().apply(MutableStateFlow<Notifier>::notify)
    runTest {
      flow.test {
        awaitItem()
        flow.notify()
        assertThat(awaitItem()).isSameAs(Notifier.initial)
      }
    }
  }
}

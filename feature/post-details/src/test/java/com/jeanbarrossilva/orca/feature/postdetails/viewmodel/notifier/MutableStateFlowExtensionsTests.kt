/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier

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

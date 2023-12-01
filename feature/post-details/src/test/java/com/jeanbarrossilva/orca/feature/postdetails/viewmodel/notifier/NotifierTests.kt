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

import assertk.assertThat
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSameAs
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.access
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.get
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

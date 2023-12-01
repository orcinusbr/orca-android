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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FlowExtensionsTests {
  @Test
  fun associates() {
    runTest {
      flowOf(8, 12)
        .associateWith { it * it }
        .test {
          assertThat(awaitItem()).isEqualTo(64 to 8)
          assertThat(awaitItem()).isEqualTo(144 to 12)
          awaitComplete()
        }
    }
  }

  @Test
  fun mapsConditionally() {
    runTest {
      flowOf(32, 64, 105, 128)
        .map({ it % 2 == 0 }) { it * 2 }
        .test {
          assertThat(awaitItem()).isEqualTo(64)
          assertThat(awaitItem()).isEqualTo(128)
          assertThat(awaitItem()).isEqualTo(105)
          assertThat(awaitItem()).isEqualTo(256)
          awaitComplete()
        }
    }
  }

  @Test
  fun comparesNotNull() {
    runTest {
      flowOf(2, 3)
        .compareNotNull { previous, current -> (previous.getOrNull() ?: current) * current }
        .test {
          assertThat(awaitItem()).isEqualTo(4)
          assertThat(awaitItem()).isEqualTo(6)
          awaitComplete()
        }
    }
  }
}

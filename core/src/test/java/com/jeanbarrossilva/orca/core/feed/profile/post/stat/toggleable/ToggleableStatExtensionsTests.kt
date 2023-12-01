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

package com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ToggleableStatExtensionsTests {
  @Test
  fun buildsToggleableStatWithConfiguredSetEnabled() {
    var isEnabled = false
    runTest {
      ToggleableStat<Int>(count = 1) { setEnabled { isEnabled = it } }
        .apply {
          disable()
          enable()
        }
    }
    assertTrue(isEnabled)
  }
}

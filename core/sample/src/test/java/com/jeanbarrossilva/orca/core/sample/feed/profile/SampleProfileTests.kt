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

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.sample.test.assertTogglingEquals
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SampleProfileTests {
  @Test
  fun `GIVEN a public unfollowed profile WHEN toggling its follow status THEN it's followed`() {
    runTest { assertTogglingEquals(Follow.Public.following(), Follow.Public.unfollowed()) }
  }

  @Test
  fun `GIVEN a public followed profile WHEN toggling its follow status THEN it's not followed`() {
    runTest { assertTogglingEquals(Follow.Public.unfollowed(), Follow.Public.following()) }
  }

  @Test
  fun `GIVEN a private unfollowed profile WHEN toggling its follow status THEN it's requested`() {
    runTest { assertTogglingEquals(Follow.Private.requested(), Follow.Private.unfollowed()) }
  }

  @Test
  fun `GIVEN a private requested profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.requested()) }
  }

  @Test
  fun `GIVEN a private followed profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.following()) }
  }
}

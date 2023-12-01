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

package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.test.TestPost
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class PostTests {
  @Test
  fun `GIVEN an unliked post WHEN liking it THEN it's liked`() {
    val post = TestPost()
    runTest {
      post.favorite.disable()
      post.favorite.enable()
      assertTrue(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a liked post WHEN unliking it THEN it isn't liked`() {
    val post = TestPost()
    runTest {
      post.favorite.enable()
      post.favorite.disable()
      assertFalse(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a post WHEN reposting it THEN it's reposted`() {
    val post = TestPost()
    runTest {
      post.repost.disable()
      post.repost.enable()
      assertTrue(post.repost.isEnabled)
    }
  }

  @Test
  fun `GIVEN a repost WHEN unreposting it THEN it isn't reposted`() {
    val post = TestPost()
    runTest {
      post.repost.enable()
      post.repost.disable()
      assertFalse(post.repost.isEnabled)
    }
  }
}

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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import kotlinx.coroutines.flow.update

/**
 * Performs [Post]-related writing operations.
 *
 * @param postProvider [SamplePostProvider] by which [Post]s will be provided.
 */
class SamplePostWriter internal constructor(private val postProvider: SamplePostProvider) {
  /** Clears all added [Post]s, including the default ones. */
  fun clear() {
    postProvider.postsFlow.update { emptyList() }
  }

  /** Resets this [SamplePostWriter] to its default state. */
  fun reset() {
    postProvider.postsFlow.value = postProvider.defaultPosts
  }
}

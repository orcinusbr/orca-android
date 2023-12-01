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
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * [PostProvider] that provides sample [Post]s.
 *
 * @param defaultPosts [Post]s that are present by default.
 */
class SamplePostProvider internal constructor(internal val defaultPosts: List<Post>) :
  PostProvider {
  /** [MutableStateFlow] that provides the [Post]s. */
  internal val postsFlow = MutableStateFlow(defaultPosts)

  override suspend fun provide(id: String): Flow<Post> {
    return postsFlow.mapNotNull { posts -> posts.find { post -> post.id == id } }
  }

  /**
   * Provides the [Post]s made by the author whose ID equals to the given one.
   *
   * @param authorID ID of the author whose [Post]s will be provided.
   */
  internal fun provideBy(authorID: String): Flow<List<Post>> {
    return postsFlow.map { posts -> posts.filter { post -> post.author.id == authorID } }
  }
}

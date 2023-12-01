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

package com.jeanbarrossilva.orca.core.sample.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * [FeedProvider] that provides a feed for a sample [Profile].
 *
 * @param profileAvatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by
 *   which [Image]s can be loaded from a [SampleImageSource].
 * @param postProvider [SamplePostProvider] by which [Post]s will be provided.
 */
internal class SampleFeedProvider(
  private val profileAvatarLoaderProvider: ImageLoader.Provider<SampleImageSource>,
  private val postProvider: SamplePostProvider
) : FeedProvider() {
  override val termMuter = SampleTermMuter()

  /** [Flow] with the posts to be provided in the feed. */
  private val postsFlow = postProvider.postsFlow.asStateFlow()

  override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
    return postsFlow.map {
      it.chunked(SampleProfile.POSTS_PER_PAGE).getOrElse(page) { emptyList() }
    }
  }

  override suspend fun containsUser(userID: String): Boolean {
    return userID == Profile.createSample(postProvider, profileAvatarLoaderProvider).id
  }
}

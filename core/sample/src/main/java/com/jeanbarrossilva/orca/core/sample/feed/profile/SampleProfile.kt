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

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** [Profile] whose operations are performed in memory and serves as a sample. */
internal interface SampleProfile : Profile {
  /** [SamplePostProvider] by which this [SampleProfile]'s [Post]s will be provided. */
  val postProvider: SamplePostProvider

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    return postProvider.provideBy(id).filterNotNull().map {
      it.windowed(POSTS_PER_PAGE, partialWindows = true).getOrElse(page) { emptyList() }
    }
  }

  companion object {
    /** Maximum amount of [Post]s emitted to [getPosts]. */
    const val POSTS_PER_PAGE = 50
  }
}

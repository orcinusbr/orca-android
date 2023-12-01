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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [MastodonPostPaginator] that paginates through a [MastodonProfile]'s [Post]s.
 *
 * @param id ID of the [MastodonProfile].
 */
internal class MastodonProfilePostPaginator(
  override val imageLoaderProvider: ImageLoader.Provider<URL>,
  id: String
) : MastodonPostPaginator() {
  override val route = "/api/v1/accounts/$id/statuses"

  /** Provides a [MastodonProfilePostPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonProfilePostPaginator] that paginates through the [Post]s of a
     * [MastodonProfile] identified as [id].
     *
     * @param id ID of the [MastodonProfile].
     */
    fun provide(id: String): MastodonProfilePostPaginator
  }
}

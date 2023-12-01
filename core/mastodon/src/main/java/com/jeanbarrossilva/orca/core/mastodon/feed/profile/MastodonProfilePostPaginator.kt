/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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

/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonStatusesPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL

/**
 * [MastodonPostPaginator] that paginates through a [MastodonAccount]'s [MastodonStatus]es.
 *
 * @param id ID of the [MastodonAccount].
 * @see MastodonAccount.id
 */
internal class MastodonProfilePostPaginator(
  override val imageLoaderProvider: SomeImageLoaderProvider<URL>,
  override val commentPaginatorProvider: MastodonCommentPaginator.Provider,
  id: String
) : MastodonStatusesPaginator() {
  override val route = "/api/v1/accounts/$id/statuses"

  /** Provides a [MastodonProfilePostPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonProfilePostPaginator] that paginates through the [MastodonStatus]es of a
     * [MastodonAccount] identified as [id].
     *
     * @param id ID of the [MastodonAccount].
     * @see MastodonAccount.id
     */
    fun provide(id: String): MastodonProfilePostPaginator
  }
}

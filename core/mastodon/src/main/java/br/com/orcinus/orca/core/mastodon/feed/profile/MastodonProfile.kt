/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.net.URL
import kotlinx.coroutines.flow.Flow

/**
 * [Profile] whose [Post]s are obtained through pagination performed by the [postPaginator].
 *
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [Post]s will be provided.
 */
internal data class MastodonProfile(
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: Markdown,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) : Profile {
  private lateinit var postPaginator: MastodonProfilePostPaginator

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    if (!::postPaginator.isInitialized) {
      postPaginator = postPaginatorProvider.provide(id)
    }
    return postPaginator.paginateTo(page)
  }
}

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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import java.net.URL

/**
 * [Fetcher] that requests [ProfileSearchResult]s to the API.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [ProfileSearchResult]s' avatars will be loaded from a [URL].
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through a [MastodonProfile]'s [Post]s will be
 *   provided.
 */
internal class MastodonProfileSearchResultsFetcher(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider
) : Fetcher<List<ProfileSearchResult>>() {
  override suspend fun onFetch(key: String): List<ProfileSearchResult> {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
      .client
      .authenticateAndGet("/api/v1/accounts/search") { parameter("q", key) }
      .body<List<MastodonAccount>>()
      .map { it.toProfile(avatarLoaderProvider, postPaginatorProvider).toProfileSearchResult() }
  }
}

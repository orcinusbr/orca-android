/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.cache

import android.content.Context
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.client.authenticateAndGet
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.cache.Fetcher
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URI

/**
 * [Fetcher] for [MastodonProfile]s.
 *
 * @param context [Context] with which a fetched [MastodonAccount] will be converted into a
 *   [Profile].
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Profile]s' avatars will be loaded from a [URI].
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through a [MastodonProfile]'s [Post]s will be
 *   provided.
 * @see MastodonAccount.toProfile
 */
internal class MastodonProfileFetcher(
  private val context: Context,
  private val avatarLoaderProvider: SomeImageLoaderProvider<URI>,
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider
) : Fetcher<Profile>() {
  override suspend fun onFetch(key: String): Profile {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .client
      .authenticateAndGet("/api/v1/accounts/$key")
      .body<MastodonAccount>()
      .toProfile(context, avatarLoaderProvider, postPaginatorProvider)
  }
}

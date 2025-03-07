/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.toggleable

import android.content.Context
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.std.func.monad.onEach
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import io.ktor.client.call.body
import java.net.URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * [ToggleableStat] for toggling the "reposted" status of a [MastodonPost]. Both the obtainance that
 * produces the [List] with those who have reposted it and the reposting/unreposting itself are
 * performed through the API.
 *
 * @param count Initial amount of [Profile]s by which the [MastodonPost] has been reposted.
 * @param isReposted Whether the [MastodonPost] is initially reposted.
 * @property context [Context] with which the fetched [MastodonAccount]s are converted into
 *   [Profile]s.
 * @property requester [Requester] by which the network calls are made.
 * @property profilePostPaginatorProvider Paginates through the [MastodonPost]s of converted
 *   [Profile]s.
 * @property avatarLoaderProvider [ImageLoader.Provider] that loads the avatar of converted
 *   [Profile]s.
 * @property id ID of the [MastodonPost].
 * @see MastodonAccount.toProfile
 * @see MastodonPost.id
 */
internal class MastodonRepostStat(
  private val context: Context,
  private val requester: Requester<*>,
  private val profilePostPaginatorProvider: MastodonProfilePostPaginator.Provider,
  private val avatarLoaderProvider: SomeImageLoaderProvider<URI>,
  private val id: String,
  count: Int,
  isReposted: Boolean
) : ToggleableStat<Profile>(isReposted, count) {
  override fun get(page: Int): Flow<List<Profile>> {
    return flow {
      emit(
        requester
          .get({ path("api").path("v1").path("statuses").path(id).path("reblogged_by").build() })
          .map { it.body<List<MastodonAccount>>() }
          .onEach {
            it.toProfile(context, requester, avatarLoaderProvider, profilePostPaginatorProvider)
          }
          .getValueOrThrow()
      )
    }
  }

  override suspend fun onSetEnabled(isEnabled: Boolean) {
    requester
      .authenticated()
      .post({
        path("api")
          .path("v1")
          .path(id)
          .run { if (isEnabled) path("reblog") else path("unreblog") }
          .build()
      })
  }
}

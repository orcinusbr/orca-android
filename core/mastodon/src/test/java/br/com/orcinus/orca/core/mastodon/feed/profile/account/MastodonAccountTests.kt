/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import kotlin.test.Test

internal class MastodonAccountTests {
  @Test
  fun convertsIntoProfileSearchResult() {
    with(Profile.sample) {
      val avatarURI =
        URIBuilder.url()
          .scheme("https")
          .host("orca.orcinus.com.br")
          .path("api")
          .path("v1")
          .path("accounts")
          .path(id)
          .path("avatar")
          .build()
      assertThat(
          MastodonAccount(
              id,
              account.username.toString(),
              acct = "$account",
              "$uri",
              displayName = name,
              locked = (this as? FollowableProfile<*>)?.follow is Follow.Private,
              note = "$bio",
              avatar = "$avatarURI",
              followersCount = followerCount,
              followingCount
            )
            .toProfileSearchResult(avatarLoaderProvider = AsyncImageLoader.Provider)
        )
        .isEqualTo(
          ProfileSearchResult(id, account, AsyncImageLoader.Provider.provide(avatarURI), name, uri)
        )
    }
  }
}

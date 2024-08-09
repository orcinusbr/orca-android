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
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import kotlin.test.Test

internal class MastodonAccountTests {
  @Test
  fun convertsIntoProfileSearchResult() {
    val profile =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
        .provideCurrent<Profile>()
    val avatarURI =
      URIBuilder.url()
        .scheme("https")
        .host("orca.orcinus.com.br")
        .path("api")
        .path("v1")
        .path("accounts")
        .path(profile.id)
        .path("avatar")
        .build()
    assertThat(
        MastodonAccount(
            profile.id,
            profile.account.username.toString(),
            acct = "${profile.account}",
            "${profile.uri}",
            displayName = profile.name,
            locked = (profile as? FollowableProfile<*>)?.follow is Follow.Private,
            note = "${profile.bio}",
            avatar = "$avatarURI",
            followersCount = profile.followerCount,
            profile.followingCount
          )
          .toProfileSearchResult(avatarLoaderProvider = AsyncImageLoader.Provider)
      )
      .isEqualTo(
        ProfileSearchResult(
          profile.id,
          profile.account,
          AsyncImageLoader.Provider.provide(avatarURI),
          profile.name,
          profile.uri
        )
      )
  }
}

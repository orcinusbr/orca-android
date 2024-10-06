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
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class MastodonAccountsTests {
  @Test
  fun isOwned() {
    val actorProvider = ActorProvider.sample
    val authenticationLock = AuthenticationLock(actorProvider)
    runTest {
      val actor = actorProvider.provide() as Actor.Authenticated
      assertThat(MastodonAccount.default.copy(id = actor.id))
        .suspendCall("isOwned") { it.isOwned(authenticationLock) }
        .isTrue()
    }
  }

  @Test
  fun isNotOwned() {
    val actorProvider = ActorProvider.sample
    val authenticationLock = AuthenticationLock(actorProvider)
    runTest {
      assertThat(MastodonAccount.default)
        .suspendCall("isOwned") { it.isOwned(authenticationLock) }
        .isFalse()
    }
  }

  @Test
  fun convertsIntoProfileSearchResult() {
    val composer =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
        .provideCurrent<Composer>()
    val avatarURI =
      URIBuilder.url()
        .scheme("https")
        .host("orca.orcinus.com.br")
        .path("api")
        .path("v1")
        .path("accounts")
        .path(composer.id)
        .path("avatar")
        .build()
    assertThat(
        MastodonAccount(
            composer.id,
            composer.account.username.toString(),
            acct = "${composer.account}",
            "${composer.uri}",
            displayName = composer.name,
            locked = (composer as? FollowableProfile<*>)?.follow is Follow.Private,
            note = "${composer.bio}",
            avatar = "$avatarURI",
            followersCount = composer.followerCount,
            composer.followingCount
          )
          .toProfileSearchResult(avatarLoaderProvider = AsyncImageLoader.Provider)
      )
      .isEqualTo(
        ProfileSearchResult(
          composer.id,
          composer.account,
          AsyncImageLoader.Provider.provide(avatarURI),
          composer.name,
          composer.uri
        )
      )
  }
}

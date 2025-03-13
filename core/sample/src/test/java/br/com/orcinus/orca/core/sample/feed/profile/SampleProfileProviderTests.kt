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

package br.com.orcinus.orca.core.sample.feed.profile

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.account.at
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.feed.profile.post.createRamboSample
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.SampleEditableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import br.com.orcinus.orca.std.markdown.Markdown
import java.util.UUID
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SampleProfileProviderTests {
  @Test
  fun isInitiallyEmpty() {
    val profiles = SampleProfileProvider().provideCurrent()
    assertThat(profiles).isEmpty()
  }

  @Test
  fun providesCurrentOneOfASpecificType() {
    val profileProvider = SampleProfileProvider()
    val profile =
      SampleFollowableProfile(
        /* delegate = */ Author.createRamboSample(NoOpSampleImageLoader.Provider),
        /* bio = */ Markdown.empty,
        Follow.Public.following(),
        /* followerCount = */ 0,
        /* followingCount = */ 0
      )
    profileProvider.add(profile)
    val providedProfile = profileProvider.provideCurrent<SampleFollowableProfile<Follow.Public>>()
    assertThat(providedProfile).isSameAs(profile)
  }

  @Test
  fun providesCurrentOnes() {
    val profileProvider = SampleProfileProvider()
    val profiles =
      arrayOf(
        SampleEditableProfile(
          profileProvider,
          delegate = Author.sample,
          bio = Markdown.empty,
          followerCount = 0,
          followingCount = 0
        ),
        SampleFollowableProfile(
          /* delegate = */ Author.createRamboSample(NoOpSampleImageLoader.Provider),
          /* bio = */ Markdown.empty,
          Follow.Public.following(),
          /* followerCount = */ 0,
          /* followingCount = */ 0
        )
      )
    profileProvider.add(*profiles)
    val providedProfiles = profileProvider.provideCurrent()
    assertThat(providedProfiles).containsExactly(*profiles)
  }

  @Test
  fun providesCurrentIdentifiedAsTheSpecifiedID() {
    val profileProvider = SampleProfileProvider()
    val profile =
      SampleEditableProfile(
        profileProvider,
        delegate = Author.sample,
        bio = Markdown.empty,
        followerCount = 0,
        followingCount = 0
      )
    profileProvider.add(profile)
    assertThat(profileProvider)
      .transform("provideCurrent") { it.provideCurrent(Actor.Authenticated.sample.id) }
      .isSuccessful()
      .isSameInstanceAs(profile)
  }

  @Test
  fun adds() {
    val profileProvider = SampleProfileProvider()
    val profileDelegateID = "${UUID.randomUUID()}"
    val profileDelegateAvatarLoader = NoOpSampleImageLoader.Provider.provide(SampleImageSource.None)
    val profileDelegate =
      Author(
        profileDelegateID,
        profileDelegateAvatarLoader,
        name = "Pete Steinberger",
        account = "steipete" at "mastodon.social",
        profileURI =
          URIBuilder.url().scheme("https").host("mastodon.social").path("@steipete").build()
      )
    val addedProfile =
      SampleFollowableProfile(
        profileDelegate,
        /* bio = */ Markdown.empty,
        Follow.Public.unfollowed(),
        /* followerCount = */ 3_604,
        /* followingCount = */ 364
      )
    profileProvider.add(addedProfile)
    val providedProfiles = profileProvider.provideCurrent()
    assertThat(providedProfiles).containsExactly(addedProfile)
  }

  @Test
  fun updates() {
    val profileProvider = SampleProfileProvider()
    val profileDelegate = Author.createRamboSample(NoOpSampleImageLoader.Provider)
    val profile =
      SampleFollowableProfile(
        profileDelegate,
        /* bio = */ Markdown.empty,
        Follow.Public.following(),
        /* followerCount = */ 0,
        /* followingCount = 0 */ 0
      )
    val profileUpdatedBio = Markdown.unstyled("😎")
    profileProvider.add(profile)
    runTest {
      profileProvider.update(profile.id) {
        @Suppress("UNCHECKED_CAST")
        (this as SampleFollowableProfile<Follow.Public>).withBio(profileUpdatedBio)
      }
    }
    assertThat(profileProvider)
      .transform("provideCurrent") { it.provideCurrent(profile.id) }
      .isSuccessful()
      .prop(Composer::bio)
      .isEqualTo(profileUpdatedBio)
  }
}

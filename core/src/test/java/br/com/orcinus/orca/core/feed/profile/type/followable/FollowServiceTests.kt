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

package br.com.orcinus.orca.core.feed.profile.type.followable

import assertk.assertThat
import assertk.assertions.isSameAs
import assertk.assertions.isSameInstanceAs
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.monad.Maybe
import br.com.orcinus.orca.std.func.test.monad.isFailed
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class FollowServiceTests {
  @Test
  fun toggles() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val composer = profileProvider.provideCurrent<SampleFollowableProfile<Follow>>()
    lateinit var setFollow: Follow
    val followService =
      object : FollowService() {
        override val profileProvider = profileProvider

        override suspend fun <T : Follow> setFollow(
          profile: FollowableProfile<T>,
          follow: T
        ): Maybe<*, Unit> {
          setFollow = follow
          return Maybe.successful()
        }

        override fun createNonFollowableProfileException(profileID: String) =
          NonFollowableProfileException(cause = null)
      }
    runTest { followService.toggle(composer.id) }
    assertThat(setFollow).isSameAs(composer.follow.toggled())
  }

  @Test
  fun failsWhenSettingNextStatusAndSettingTheFollowStatusFails() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val profileID = profileProvider.provideCurrent<SampleFollowableProfile<*>>().id
    val exception = Exception()
    val followService =
      object : FollowService() {
        override val profileProvider = profileProvider

        override suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T) =
          Maybe.failed<_, Unit>(exception)

        override fun createNonFollowableProfileException(profileID: String) =
          NonFollowableProfileException(cause = null)
      }
    runTest {
      assertThat(followService)
        .suspendCall("next") { it.next(profileID) }
        .isFailed()
        .isSameInstanceAs(exception)
    }
  }

  @Test
  fun setsNextStatus() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val composer = profileProvider.provideCurrent<SampleFollowableProfile<Follow>>()
    lateinit var setFollow: Follow
    val followService =
      object : FollowService() {
        override val profileProvider = profileProvider

        override suspend fun <T : Follow> setFollow(
          profile: FollowableProfile<T>,
          follow: T
        ): Maybe<*, Unit> {
          setFollow = follow
          return Maybe.successful()
        }

        override fun createNonFollowableProfileException(profileID: String) =
          NonFollowableProfileException(cause = null)
      }
    runTest { followService.next(composer.id) }
    assertThat(setFollow).isSameAs(composer.follow.next())
  }
}

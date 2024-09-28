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

package br.com.orcinus.orca.core.sample.feed.profile

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import kotlin.test.assertEquals

/**
 * Asserts that toggling an [SampleFollowableProfile]'s follow status that's been initially set to
 * [before] results in [after].
 *
 * @param before [Follow] status before the toggle.
 * @param after [Follow] status after the toggle.
 * @see SampleFollowableProfile.follow
 * @see SampleFollowService.toggle
 */
internal suspend fun <T : Follow> assertTogglingEquals(after: T, before: T) {
  val profileProvider = SampleProfileProvider()
  val profileDelegate = Author.createSample(NoOpSampleImageLoader.Provider)
  val profile =
    SampleFollowableProfile(
      profileDelegate,
      Markdown.empty,
      /* follow = */ before,
      /* followerCount = */ 0,
      /* followingCount = */ 0
    )
  val followService = SampleFollowService(profileProvider)
  profileProvider.add(profile)
  followService.toggle(profile.id, profile.follow)
  assertEquals(after, profileProvider.provideCurrent<SampleFollowableProfile<T>>().follow)
}

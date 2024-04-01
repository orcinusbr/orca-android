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

package br.com.orcinus.orca.core.sample.test

import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.createSample
import br.com.orcinus.orca.core.sample.test.instance.sample
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach

/**
 * Asserts that toggling an [SampleFollowableProfile]'s [follow][SampleFollowableProfile.follow]
 * status that's been initially set to [before] results in [after].
 *
 * @param before [Follow] status before the [toggle][SampleFollowableProfile.toggleFollow].
 * @param after [Follow] status after the [toggle][SampleFollowableProfile.toggleFollow].
 */
internal suspend fun <T : Follow> assertTogglingEquals(after: T, before: T) {
  val instance = Instance.sample
  val profile =
    FollowableProfile.createSample(
      instance.profileWriter,
      instance.postProvider,
      follow = before,
      instance.imageLoaderProvider
    )
  val matchingAfter = Follow.requireVisibilityMatch(before, after)
  instance.profileWriter.insert(profile)
  assertEquals(
    matchingAfter,
    instance.profileProvider
      .provide(profile.id)
      .filterIsInstance<FollowableProfile<T>>()
      .onEach(FollowableProfile<T>::toggleFollow)
      .drop(1)
      .first()
      .follow
  )
  instance.profileWriter.delete(profile.id)
}

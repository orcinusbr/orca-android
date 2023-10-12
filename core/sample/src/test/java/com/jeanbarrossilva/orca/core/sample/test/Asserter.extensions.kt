package com.jeanbarrossilva.orca.core.sample.test

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.sample
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
  val matchingAfter = Follow.requireVisibilityMatch(before, after)
  val profile =
    @Suppress("UNCHECKED_CAST")
    (FollowableProfile.sample as SampleFollowableProfile<T>).copy(follow = before)

  SampleProfileWriter.insert(profile)
  assertEquals(
    matchingAfter,
    SampleProfileProvider.provide(profile.id)
      .filterIsInstance<FollowableProfile<T>>()
      .onEach(FollowableProfile<T>::toggleFollow)
      .drop(1)
      .first()
      .follow
  )
}

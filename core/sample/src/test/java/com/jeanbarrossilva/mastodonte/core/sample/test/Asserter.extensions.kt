package com.jeanbarrossilva.mastodonte.core.sample.test

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.SampleFollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
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
 **/
internal suspend fun <T : Follow> assertTogglingEquals(after: T, before: T) {
    val matchingAfter = Follow.requireVisibilityMatch(before, after)
    val profile =
        @Suppress("UNCHECKED_CAST")
        (FollowableProfile.sample as SampleFollowableProfile<T>).copy(follow = before)

    SampleProfileDao.insert(profile)
    assertEquals(
        matchingAfter,
        SampleProfileDao
            .get(profile.id)
            .filterIsInstance<FollowableProfile<T>>()
            .onEach(FollowableProfile<T>::toggleFollow)
            .drop(1)
            .first()
            .follow
    )
}

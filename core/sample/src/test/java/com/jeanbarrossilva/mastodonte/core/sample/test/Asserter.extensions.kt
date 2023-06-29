package com.jeanbarrossilva.mastodonte.core.sample.test

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.SampleFollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import java.util.UUID
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest

/**
 * Asserts that toggling an [SampleFollowableProfile]'s [follow][SampleFollowableProfile.follow]
 * status that's been initially set to [before] results in [after].
 *
 * @param before [Follow] status before the [toggle][SampleFollowableProfile.toggleFollow].
 * @param after [Follow] status after the [toggle][SampleFollowableProfile.toggleFollow].
 **/
internal fun <T : Follow> assertTogglingEquals(before: T, after: T) {
    val id = UUID.randomUUID().toString()
    val matchingAfter = Follow.requireVisibilityMatch(before, after)
    SampleProfileDao.insert(
        id,
        Profile.sample.account,
        Profile.sample.avatarURL,
        Profile.sample.name,
        Profile.sample.bio,
        follow = before,
        Profile.sample.followerCount,
        Profile.sample.followingCount,
        Profile.sample.url
    )
    runTest {
        assertEquals(
            matchingAfter,
            SampleProfileDao
                .get(id)
                .filterIsInstance<FollowableProfile<T>>()
                .onEach(FollowableProfile<T>::toggleFollow)
                .first()
                .follow
        )
    }
}

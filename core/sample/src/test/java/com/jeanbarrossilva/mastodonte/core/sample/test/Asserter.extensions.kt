package com.jeanbarrossilva.mastodonte.core.sample.test

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.SampleFollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import kotlin.test.assertEquals
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
    val profile = object : SampleFollowableProfile<T>() {
        override val id = FollowableProfile.sample.id
        override val account = FollowableProfile.sample.account
        override val avatarURL = FollowableProfile.sample.avatarURL
        override val name = FollowableProfile.sample.name
        override val bio = FollowableProfile.sample.bio
        override var follow = before
        override val followerCount = FollowableProfile.sample.followerCount
        override val followingCount = FollowableProfile.sample.followingCount
        override val url = FollowableProfile.sample.url
    }
    SampleProfileDao.insert(profile)
    assertEquals(
        matchingAfter,
        SampleProfileDao
            .get(profile.id)
            .filterIsInstance<FollowableProfile<T>>()
            .onEach(FollowableProfile<T>::toggleFollow)
            .first()
            .follow
    )
}

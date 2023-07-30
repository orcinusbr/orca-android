package com.jeanbarrossilva.mastodonte.core.sample.profile.type.follow

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.type.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.type.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample

/** [FollowableProfile] that's returned by [sample]'s getter. **/
private val sampleFollowableProfile: FollowableProfile<*> = SampleFollowableProfile(
    Profile.sample.id,
    Profile.sample.account,
    Profile.sample.avatarURL,
    Profile.sample.name,
    Profile.sample.bio,
    Follow.Public.following(),
    Profile.sample.followerCount,
    Profile.sample.followingCount,
    Profile.sample.url
)

/** A sample [FollowableProfile]. **/
val FollowableProfile.Companion.sample
    get() = sampleFollowableProfile

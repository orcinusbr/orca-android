package com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample

/** [FollowableProfile] that's returned by [sample]'s getter. */
private val sampleFollowableProfile: FollowableProfile<*> =
  SampleFollowableProfile(
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

/** A sample [FollowableProfile]. */
val FollowableProfile.Companion.sample
  get() = sampleFollowableProfile

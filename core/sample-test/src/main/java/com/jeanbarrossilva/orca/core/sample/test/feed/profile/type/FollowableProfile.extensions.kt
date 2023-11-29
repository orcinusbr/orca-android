package com.jeanbarrossilva.orca.core.sample.test.feed.profile.type

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample

/** [FollowableProfile] returned by [sample]. */
private val testSampleFollowableProfile =
  FollowableProfile.createSample(
    Instance.sample.profileWriter,
    Instance.sample.postProvider,
    Follow.Public.following(),
    TestSampleImageLoader.Provider
  )

/** Test sample [FollowableProfile]. */
val FollowableProfile.Companion.sample
  get() = testSampleFollowableProfile

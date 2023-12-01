/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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

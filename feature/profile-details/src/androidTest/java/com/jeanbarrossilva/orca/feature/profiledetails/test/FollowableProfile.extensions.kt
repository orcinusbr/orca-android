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

package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.platform.ui.core.image.sample
import com.jeanbarrossilva.orca.platform.ui.core.sample
import com.jeanbarrossilva.orca.platform.ui.core.withSample
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [FollowableProfile] returned by [withSample]. */
private val sampleFollowableProfile =
  FollowableProfile.createSample(
    Instance.sample.profileWriter,
    Instance.sample.postProvider,
    FollowableProfile.sample.follow,
    ComposableImageLoader.Provider.sample
  )

/** Sample [FollowableProfile] whose images are loaded by a [ComposableImageLoader]. */
internal val FollowableProfile.Companion.sample
  get() = sampleFollowableProfile

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

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.text.toAnnotatedString
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.toStatus

/**
 * Creates a sample [ProfileDetails.Followable].
 *
 * @param onStatusToggle Operation to be performed whenever the [ProfileDetails.Followable]'s
 *   [status][ProfileDetails.Followable.status] is changed.
 */
internal fun ProfileDetails.Followable.Companion.createSample(
  onStatusToggle: () -> Unit
): ProfileDetails.Followable {
  val profile =
    FollowableProfile.createSample(
      Instance.sample.profileWriter,
      Instance.sample.postProvider,
      Follow.Public.following(),
      TestSampleImageLoader.Provider
    )
  return ProfileDetails.Followable(
    profile.id,
    profile.avatarLoader,
    profile.name,
    profile.account,
    profile.bio.toAnnotatedString(Colors.LIGHT),
    profile.url,
    profile.follow.toStatus(),
    onStatusToggle
  )
}

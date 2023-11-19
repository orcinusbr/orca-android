package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.toStatus
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

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
      Instance.sample.tootProvider,
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

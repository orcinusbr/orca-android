package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [ProfileConverter] that converts a [FollowableProfile].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]'s [Follow] status toggle
 *   will be performed.
 */
internal class FollowableProfileConverter(
  private val coroutineScope: CoroutineScope,
  override val next: ProfileConverter?
) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails? {
    return if (profile is FollowableProfile<*>) {
      ProfileDetails.Followable(
        profile.id,
        profile.avatarLoader,
        profile.name,
        profile.account,
        profile.bio.toAnnotatedString(colors),
        profile.url,
        profile.follow.toStatus()
      ) {
        coroutineScope.launch { profile.toggleFollow() }
      }
    } else {
      null
    }
  }
}

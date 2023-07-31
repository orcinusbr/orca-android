package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.ProfileConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [ProfileConverter] that converts a [FollowableProfile].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]'s [Follow] status toggle
 * will be performed.
 **/
internal class FollowableProfileConverter(
    private val coroutineScope: CoroutineScope,
    override val next: ProfileConverter?
) : ProfileConverter() {
    override fun onConvert(profile: Profile): ProfileDetails? {
        return if (profile is FollowableProfile<*>) {
            ProfileDetails.Followable(
                profile.id,
                profile.avatarURL,
                profile.name,
                profile.account,
                profile.bio,
                profile.url,
                profile.follow.toStatus()
            ) {
                coroutineScope.launch {
                    profile.toggleFollow()
                }
            }
        } else {
            null
        }
    }
}

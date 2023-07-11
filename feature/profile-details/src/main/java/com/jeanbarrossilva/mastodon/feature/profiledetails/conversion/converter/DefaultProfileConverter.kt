package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.mastodonte.core.profile.Profile

/**
 * [ProfileConverter] that converts a [Profile], regardless of its type, into a
 * [ProfileDetails.Default].
 **/
internal class DefaultProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
    override fun onConvert(profile: Profile): ProfileDetails {
        return ProfileDetails.Default(
            profile.id,
            profile.avatarURL,
            profile.name,
            profile.account,
            profile.bio,
            profile.url
        )
    }
}

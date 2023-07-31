package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.ProfileConverter

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

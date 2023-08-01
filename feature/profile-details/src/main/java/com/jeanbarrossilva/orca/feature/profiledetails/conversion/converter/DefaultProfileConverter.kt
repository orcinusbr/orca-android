package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter

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

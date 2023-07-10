package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile

/** [ProfileConverter] that converts an [EditableProfile]. **/
internal class EditableProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
    override fun onConvert(profile: Profile): ProfileDetails? {
        return if (profile is EditableProfile) {
            ProfileDetails.Editable(
                profile.id,
                profile.avatarURL,
                profile.name,
                profile.account,
                profile.bio,
                profile.url
            )
        } else {
            null
        }
    }
}

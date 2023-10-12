package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** [ProfileConverter] that converts an [EditableProfile]. */
internal class EditableProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails? {
    return if (profile is EditableProfile) {
      ProfileDetails.Editable(
        profile.id,
        profile.avatarURL,
        profile.name,
        profile.account,
        profile.bio.toAnnotatedString(colors),
        profile.url
      )
    } else {
      null
    }
  }
}

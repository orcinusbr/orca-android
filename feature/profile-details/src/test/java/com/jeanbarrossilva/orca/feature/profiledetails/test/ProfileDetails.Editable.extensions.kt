package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** Sample [ProfileDetails.Editable]. */
internal val ProfileDetails.Editable.Companion.sample
  get() =
    ProfileDetails.Editable(
      EditableProfile.sample.id,
      EditableProfile.sample.avatarLoader,
      EditableProfile.sample.name,
      EditableProfile.sample.account,
      EditableProfile.sample.bio.toAnnotatedString(Colors.Unspecified),
      EditableProfile.sample.url
    )

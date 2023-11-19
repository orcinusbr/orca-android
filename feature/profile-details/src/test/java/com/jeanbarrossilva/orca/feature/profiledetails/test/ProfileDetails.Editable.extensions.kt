package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** Sample [ProfileDetails.Editable]. */
internal val ProfileDetails.Editable.Companion.sample
  get() =
    ProfileDetails.Editable(
      EditableProfile.sample.id,
      EditableProfile.sample.avatarLoader,
      EditableProfile.sample.name,
      EditableProfile.sample.account,
      EditableProfile.sample.bio.toAnnotatedString(Colors.LIGHT),
      EditableProfile.sample.url
    )

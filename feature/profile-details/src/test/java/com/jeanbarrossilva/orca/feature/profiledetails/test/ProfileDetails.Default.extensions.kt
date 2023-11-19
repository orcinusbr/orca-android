package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** Sample [ProfileDetails.Default]. */
internal val ProfileDetails.Default.Companion.sample
  get() =
    ProfileDetails.Default(
      Profile.sample.id,
      Profile.sample.avatarLoader,
      Profile.sample.name,
      Profile.sample.account,
      Profile.sample.bio.toAnnotatedString(Colors.LIGHT),
      Profile.sample.url
    )

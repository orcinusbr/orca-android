/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/**
 * [ProfileConverter] that converts a [Profile], regardless of its type, into a
 * [ProfileDetails.Default].
 */
internal class DefaultProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails {
    return ProfileDetails.Default(
      profile.id,
      profile.avatarLoader,
      profile.name,
      profile.account,
      profile.bio.toAnnotatedString(colors),
      profile.url
    )
  }
}

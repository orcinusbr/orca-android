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

package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.timeline.text.toAnnotatedString
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails

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

/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.profiledetails

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.text.annotated.toAnnotatedString
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.SampleEditableProfile

/**
 * Creates a sample [ProfileDetails.Editable].
 *
 * @param profileProvider [SampleProfileProvider] from which a [Profile] to be converted into the
 *   created [ProfileDetails.Editable] is provided.
 */
internal fun ProfileDetails.Editable.Companion.createSample(
  profileProvider: SampleProfileProvider
): ProfileDetails.Editable {
  val profile = profileProvider.provideCurrent<SampleEditableProfile>()
  return ProfileDetails.Editable(
    profile.id,
    profile.avatarLoader,
    profile.name,
    profile.account,
    profile.bio.toAnnotatedString(Colors.LIGHT),
    profile.uri
  )
}

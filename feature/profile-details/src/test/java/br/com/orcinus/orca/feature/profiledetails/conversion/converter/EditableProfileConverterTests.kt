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

package br.com.orcinus.orca.feature.profiledetails.conversion.converter

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.test.feed.profile.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.type.sample
import br.com.orcinus.orca.feature.profiledetails.ProfileDetails
import br.com.orcinus.orca.feature.profiledetails.sample
import kotlin.test.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

internal class EditableProfileConverterTests {
  private val converter = EditableProfileConverter(next = null)

  @Test
  fun convertsEditableProfile() {
    assertEquals(
      ProfileDetails.Editable.sample,
      converter.convert(EditableProfile.sample, Colors.LIGHT)
    )
  }

  @Test
  fun doesNotConvertDefaultProfile() {
    assertNull(converter.convert(Profile.sample, Colors.LIGHT))
  }

  @Test
  fun doesNotConvertFollowableProfile() {
    assertNull(converter.convert(FollowableProfile.sample, Colors.LIGHT))
  }
}

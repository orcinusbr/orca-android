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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import br.com.orcinus.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.test.createSample
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class FollowableProfileConverterTests {
  private val coroutineScope = TestScope()
  private val converter = FollowableProfileConverter(coroutineScope, next = null)

  @Test
  fun convertsFollowableProfile() {
    val onStatusToggle = {}
    assertEquals(
      ProfileDetails.Followable.createSample(onStatusToggle),
      converter
        .convert(FollowableProfile.sample, Colors.LIGHT)
        .let { it as ProfileDetails.Followable }
        .copy(onStatusToggle = onStatusToggle)
    )
  }

  @Test
  fun doesNotConvertDefaultProfile() {
    assertNull(converter.convert(Profile.sample, Colors.LIGHT))
  }

  @Test
  fun doesNotConvertEditableProfile() {
    assertNull(converter.convert(EditableProfile.sample, Colors.LIGHT))
  }
}

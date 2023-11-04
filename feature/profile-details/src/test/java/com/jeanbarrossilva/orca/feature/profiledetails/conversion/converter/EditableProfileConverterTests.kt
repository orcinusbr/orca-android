package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.test.sample
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class EditableProfileConverterTests {
  private val converter = EditableProfileConverter(next = null)

  @Test
  fun convertsEditableProfile() {
    assertEquals(
      ProfileDetails.Editable.sample,
      converter.convert(EditableProfile.sample, Colors.Unspecified)
    )
  }

  @Test
  fun doesNotConvertDefaultProfile() {
    assertNull(converter.convert(Profile.sample, Colors.Unspecified))
  }

  @Test
  fun doesNotConvertFollowableProfile() {
    assertNull(converter.convert(FollowableProfile.sample, Colors.Unspecified))
  }
}

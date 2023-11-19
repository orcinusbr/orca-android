package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.test.sample
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DefaultProfileConverterTests {
  private val converter = DefaultProfileConverter(next = null)

  @Test
  fun convertsDefaultProfile() {
    assertEquals(ProfileDetails.Default.sample, converter.convert(Profile.sample, Colors.LIGHT))
  }

  @Test
  fun convertsEditableProfile() {
    assertEquals(
      ProfileDetails.Default.sample,
      converter.convert(EditableProfile.sample, Colors.LIGHT)
    )
  }

  @Test
  fun convertsFollowableProfile() {
    assertEquals(
      ProfileDetails.Default.sample,
      converter.convert(FollowableProfile.sample, Colors.LIGHT)
    )
  }
}

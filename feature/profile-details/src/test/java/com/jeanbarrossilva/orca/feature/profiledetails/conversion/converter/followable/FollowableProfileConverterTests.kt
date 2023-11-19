package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.autos.colors.Colors
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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class DefaultProfileConverterTests {
  private val converter = DefaultProfileConverter(next = null)

  @get:Rule
  val sampleCoreRule = SampleCoreTestRule()

  @Test
  fun convertsDefaultProfile() {
    assertEquals(
      ProfileDetails.Default.createSample(Colors.Unspecified),
      converter.convert(Profile.sample, Colors.Unspecified)
    )
  }

  @Test
  fun convertsEditableProfile() {
    assertEquals(
      ProfileDetails.Default.createSample(Colors.Unspecified),
      converter.convert(EditableProfile.sample, Colors.Unspecified)
    )
  }

  @Test
  fun convertsFollowableProfile() {
    assertEquals(
      ProfileDetails.Default.createSample(Colors.Unspecified),
      converter.convert(FollowableProfile.sample, Colors.Unspecified)
    )
  }
}

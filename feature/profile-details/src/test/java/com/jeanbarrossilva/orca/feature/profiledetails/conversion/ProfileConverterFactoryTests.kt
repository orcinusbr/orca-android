package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.test.createSample
import com.jeanbarrossilva.orca.feature.profiledetails.test.sample
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ProfileConverterFactoryTests {
  private val coroutineScope = TestScope()

  @Test
  fun createdConverterConvertsDefaultProfile() {
    assertEquals(
      ProfileDetails.Default.sample,
      ProfileConverterFactory.create(coroutineScope).convert(Profile.sample, Colors.Unspecified)
    )
  }

  @Test
  fun createdConverterConvertsEditableProfile() {
    assertEquals(
      ProfileDetails.Editable.sample,
      ProfileConverterFactory.create(coroutineScope)
        .convert(EditableProfile.sample, Colors.Unspecified)
    )
  }

  @Test
  fun createdConverterConvertsFollowableProfile() {
    val onStatusToggle = {}
    assertEquals(
      ProfileDetails.Followable.createSample(onStatusToggle),
      ProfileConverterFactory.create(coroutineScope)
        .convert(FollowableProfile.sample, Colors.Unspecified)
        .let { it as ProfileDetails.Followable }
        .copy(onStatusToggle = onStatusToggle)
    )
  }
}

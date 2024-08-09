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

package br.com.orcinus.orca.feature.profiledetails.conversion

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.profiledetails.ProfileDetails
import br.com.orcinus.orca.feature.profiledetails.createSample
import br.com.orcinus.orca.feature.profiledetails.getSampleDelegateProfile
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.TestScope

internal class ProfileConverterFactoryTests {
  private val coroutineScope = TestScope()

  @Test
  fun createdConverterConvertsDefaultProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val defaultProfile = ProfileDetails.Default.getSampleDelegateProfile(profileProvider)
    assertThat(ProfileConverterFactory.create(coroutineScope).convert(defaultProfile, Colors.LIGHT))
      .isEqualTo(ProfileDetails.Default.createSample(profileProvider, defaultProfile))
  }

  @Test
  fun createdConverterConvertsEditableProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val editableProfile = profileProvider.provideCurrent<EditableProfile>()
    assertThat(
        ProfileConverterFactory.create(coroutineScope).convert(editableProfile, Colors.LIGHT)
      )
      .isEqualTo(ProfileDetails.Editable.createSample(profileProvider))
  }

  @Test
  fun createdConverterConvertsFollowableProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val followableProfile = profileProvider.provideCurrent<FollowableProfile<*>>()
    val onStatusToggle = {}
    assertThat(
        ProfileConverterFactory.create(coroutineScope)
          .convert(followableProfile, Colors.LIGHT)
          .let { it as ProfileDetails.Followable }
          .copy(onStatusToggle = onStatusToggle)
      )
      .isEqualTo(ProfileDetails.Followable.createSample(profileProvider, onStatusToggle))
  }
}

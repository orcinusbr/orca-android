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

internal class DefaultProfileConverterTests {
  private val converter = DefaultProfileConverter(next = null)

  @Test
  fun convertsDefaultProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val defaultProfile = ProfileDetails.Default.getSampleDelegateProfile(profileProvider)
    val profileDetails = ProfileDetails.Default.createSample(profileProvider, defaultProfile)
    assertThat(converter.convert(defaultProfile, Colors.LIGHT)).isEqualTo(profileDetails)
  }

  @Test
  fun convertsEditableProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val editableProfile = profileProvider.provideCurrent<EditableProfile>()
    val profileDetails = ProfileDetails.Default.createSample(profileProvider, editableProfile)
    assertThat(converter.convert(editableProfile, Colors.LIGHT)).isEqualTo(profileDetails)
  }

  @Test
  fun convertsFollowableProfile() {
    val profileProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val followableProfile = profileProvider.provideCurrent<FollowableProfile<*>>()
    val profileDetails = ProfileDetails.Default.createSample(profileProvider, followableProfile)
    assertThat(converter.convert(followableProfile, Colors.LIGHT)).isEqualTo(profileDetails)
  }
}

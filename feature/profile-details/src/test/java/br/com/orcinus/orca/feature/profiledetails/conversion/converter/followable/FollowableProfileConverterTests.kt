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

package br.com.orcinus.orca.feature.profiledetails.conversion.converter.followable

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.profiledetails.ProfileDetails
import br.com.orcinus.orca.feature.profiledetails.createSample
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertNull

internal class FollowableProfileConverterTests {
  private val coroutineScope = TestScope()
  private val profileProvider =
    SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
      .withDefaultProfiles()
      .build()
      .profileProvider
  private val followService = SampleFollowService(profileProvider)
  private val converter = FollowableProfileConverter(coroutineScope, followService, next = null)

  @Test
  fun convertsFollowableProfile() {
    val onStatusToggle = {}
    assertThat(
        converter
          .convert(profileProvider.provideCurrent<FollowableProfile<*>>(), Colors.LIGHT)
          .let { it as ProfileDetails.Followable }
          .copy(onStatusToggle = onStatusToggle)
      )
      .isEqualTo(ProfileDetails.Followable.createSample(profileProvider, onStatusToggle))
  }

  @Test
  fun doesNotConvertDefaultProfile() {
    assertThat(
        converter.convert(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .build()
            .profileProvider
            .provideCurrent()
            .first { it::class.supertypes[1] == typeOf<Profile>() },
          Colors.LIGHT
        )
      )
      .isNull()
  }

  @Test
  fun doesNotConvertEditableProfile() {
    assertNull(
      converter.convert(
        SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .build()
          .profileProvider
          .provideCurrent<EditableProfile>(),
        Colors.LIGHT
      )
    )
  }
}

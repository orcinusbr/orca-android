/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.type.followable

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SampleFollowServiceTests {
  @Test
  fun toggles() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val getProfile = { profileProvider.provideCurrent<FollowableProfile<Follow.Public>>() }
    val untoggledFollowProfile = getProfile()
    runTest {
      SampleFollowService(profileProvider)
        .toggle(untoggledFollowProfile.id, untoggledFollowProfile.follow)
    }
    assertThat(getProfile())
      .prop(FollowableProfile<*>::follow)
      .isEqualTo(untoggledFollowProfile.follow.toggled())
  }
}

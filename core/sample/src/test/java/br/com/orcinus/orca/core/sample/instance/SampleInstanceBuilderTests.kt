/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.sample.instance

import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import assertk.assertions.prop
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import kotlin.test.Test

internal class SampleInstanceBuilderTests {
  @Test
  fun buildsSampleInstanceWithoutDefaultProfilesAndPosts() =
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::build)
      .prop(SampleInstance::profileProvider)
      .transform("provideCurrent") { it.provideCurrent() }
      .isEmpty()

  @Test
  fun buildsSampleInstanceWithDefaultProfilesAndWithoutDefaultPosts() =
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::withDefaultProfiles)
      .prop(SampleInstance.Builder.DefaultProfiles::build)
      .all {
        prop(SampleInstance::profileProvider)
          .transform("provideCurrent") { it.provideCurrent() }
          .isNotEmpty()
        prop(SampleInstance::feedProvider)
          .transform("provideCurrent") { it.provideCurrent(page = 0) }
          .isSuccessful()
          .isEmpty()
      }

  @Test
  fun buildsSampleInstanceWithDefaultProfilesAndPosts() =
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::withDefaultProfiles)
      .prop(SampleInstance.Builder.DefaultProfiles::withDefaultPosts)
      .prop(SampleInstance.Builder.DefaultPosts::build)
      .all {
        prop(SampleInstance::profileProvider)
          .transform("provideCurrent") { it.provideCurrent() }
          .isNotEmpty()
        prop(SampleInstance::feedProvider)
          .transform("provideCurrent") { it.provideCurrent(page = 0) }
          .isSuccessful()
          .isNotEmpty()
      }
}

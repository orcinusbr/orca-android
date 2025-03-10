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
import assertk.assertions.isNotEmpty
import assertk.assertions.prop
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import kotlin.test.Test

internal class SampleInstanceProviderFactoryTests {
  @Test
  fun createsASampleInstanceProviderThatProvidesASampleInstanceWithDefaultProfilesAndPostsByDefault() =
    assertThat(SampleInstanceProvider(NoOpSampleImageLoader.Provider))
      .prop(SampleInstanceProvider::provide)
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

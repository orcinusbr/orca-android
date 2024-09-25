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

package br.com.orcinus.orca.core.sample.instance

import assertk.assertThat
import assertk.assertions.isNotEmpty
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test

internal class SampleInstanceProviderFactoryTests {
  @Test
  fun createsASampleInstanceProviderThatProvidesASampleInstanceWithDefaultProfilesAndPostsByDefault() {
    val imageLoaderProvider = NoOpSampleImageLoader.Provider
    val providedInstance = SampleInstanceProvider(imageLoaderProvider).provide()
    val providedProfiles = providedInstance.profileProvider.provideCurrent()
    val feed = providedInstance.feedProvider.provideCurrent(Actor.Authenticated.sample.id, page = 0)
    assertThat(providedProfiles).isNotEmpty()
    assertThat(feed).isNotEmpty()
  }
}

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

package br.com.orcinus.orca.core.feed.profile

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.test.monad.isFailed
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class ProfileProviderTests {
  @Test
  fun failsWhenANonexistentProfileIsRequestedToBeProvided() = runTest {
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::build)
      .prop(SampleInstance::profileProvider)
      .suspendCall("provide") { it.provide("🫥") }
      .isFailed()
      .isInstanceOf<ProfileProvider.NonexistentProfileException>()
  }

  @Test
  fun provides() = runTest {
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::withDefaultProfiles)
      .prop(SampleInstance.Builder.DefaultProfiles::build)
      .prop(SampleInstance::profileProvider)
      .suspendCall("provide") { it.provide(Actor.Authenticated.sample.id) }
      .isSuccessful()
      .transform("test") {
        it.test {
          assertThat(awaitItem()).prop(Profile::id).isEqualTo(Actor.Authenticated.sample.id)
        }
      }
  }
}

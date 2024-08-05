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

package br.com.orcinus.core.test.auth.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.std.image.test.TestImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class FixedActorProviderTests {
  @Test
  fun providesSpecifiedActor() {
    runTest {
      val actor = Actor.Authenticated("id", "access-token", TestImageLoader)
      assertThat(FixedActorProvider(actor).provide()).isSameAs(actor)
    }
  }

  @Test
  fun providesSpecifiedActorEvenWhenAnotherOneIsRemembered() {
    runTest {
      val actor = Actor.Unauthenticated
      val actorProvider = FixedActorProvider(actor)
      Authenticator(actorProvider = actorProvider).authenticate()
      assertThat(actorProvider.provide()).isEqualTo(actor)
    }
  }
}

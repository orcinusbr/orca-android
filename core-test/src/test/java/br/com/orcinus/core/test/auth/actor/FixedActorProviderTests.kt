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

package br.com.orcinus.core.test.auth.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.auth.AuthorizationCode
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.auth.uuid
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.std.image.test.NoOpImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class FixedActorProviderTests {
  @Test
  fun providesSpecifiedActor() {
    val actor = Actor.Authenticated("id", "access-token", NoOpImageLoader)
    val actorProvider = FixedActorProvider(actor)
    runTest {
      assertThat(actorProvider)
        .suspendCall("provide", FixedActorProvider::provide)
        .isSameInstanceAs(actor)
    }
  }

  @Test
  fun providesSpecifiedActorEvenWhenAnotherOneIsRemembered() {
    val actor = Actor.Unauthenticated
    val actorProvider = FixedActorProvider(actor)
    val authorizationCode = AuthorizationCode.uuid()
    runTest {
      Authenticator(actorProvider = actorProvider).authenticate(authorizationCode)
      assertThat(actorProvider).suspendCall("provide", FixedActorProvider::provide).isEqualTo(actor)
    }
  }
}

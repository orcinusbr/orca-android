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

package br.com.orcinus.core.test.auth

import assertk.assertThat
import assertk.assertions.isSameInstanceAs
import br.com.orcinus.core.test.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.auth.AuthorizationCode
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.auth.uuid
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import br.com.orcinus.orca.std.image.test.NoOpImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AuthenticatorFactoryTests {
  @Test
  fun createdAuthenticatorProvidesActorProvidedByTheActorProviderUponAuthenticationByDefault() {
    val actor = Actor.Authenticated("id", "access-token", NoOpImageLoader)
    val actorProvider = InMemoryActorProvider().apply { remember(actor) }
    val authenticator = Authenticator(actorProvider)
    val authorizationCode = AuthorizationCode.uuid()
    runTest {
      assertThat(authenticator)
        .transform(name = "authenticate") { it.authenticate(authorizationCode) }
        .isSameInstanceAs(actor)
    }
  }

  @Test
  fun createdAuthenticatorDelegatesAuthenticationToTheSpecifiedCallback() {
    val actor = Actor.Authenticated("id", "access-token", NoOpImageLoader)
    val actorProvider = FixedActorProvider(actor)
    val authenticator = Authenticator(actorProvider) { actor }
    val authorizationCode = AuthorizationCode.uuid()
    runTest {
      assertThat(authenticator)
        .transform(name = "authenticate") { it.authenticate(authorizationCode) }
        .isSameInstanceAs(actor)
    }
  }
}

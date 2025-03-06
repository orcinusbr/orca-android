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
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.auth.AuthorizationCode
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.auth.uuid
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AuthorizerBuilderTests {
  @Test
  fun buildsAnAuthorizerWithTheSpecifiedAuthorizationCode() {
    val actorProvider = InMemoryActorProvider()
    val authorizationCode = AuthorizationCode.uuid()
    runTest {
      Authenticator(actorProvider) {
          assertThat(it).transform(transform = ::AuthorizationCode).isEqualTo(authorizationCode)
          Actor.Unauthenticated
        }
        .authenticate(authorizationCode)
    }
  }
}

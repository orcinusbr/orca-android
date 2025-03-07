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

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.core.test.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import br.com.orcinus.orca.std.image.test.NoOpImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockFactoryTests {
  @Test
  fun createdAuthenticationLockThrowsWithNullCauseWhenAnUnlockIsScheduledButAuthenticationFails() {
    val authorizer = AuthorizerBuilder().build()
    val actorProvider = FixedActorProvider(Actor.Unauthenticated)
    val authenticationLock = AuthenticationLock(authorizer, actorProvider)
    runTest {
      assertFailure { authenticationLock.scheduleUnlock {}.getValueOrThrow() }
        .isNotNull()
        .isInstanceOf<AuthenticationLock.FailedAuthenticationException>()
        .prop(AuthenticationLock.FailedAuthenticationException::cause)
        .isNull()
    }
  }

  @Test
  fun createdAuthenticationLockUnlocksWithActorProvidedByTheSpecifiedProvider() = runTest {
    val authorizer = AuthorizerBuilder().build()
    val actor = Actor.Authenticated("id", "access-token", NoOpImageLoader)
    val actorProvider = InMemoryActorProvider().apply { remember(actor) }
    AuthenticationLock(authorizer, actorProvider)
      .scheduleUnlock { assertThat(it).isSameInstanceAs(actor) }
      .getValueOrThrow()
  }
}

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

package br.com.orcinus.core.test.auth

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.core.test.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import br.com.orcinus.orca.std.image.test.TestImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockFactoryTests {
  @Test
  fun createdAuthenticationLockThrowsWithNullCauseWhenAnUnlockIsScheduledButAuthenticationFails() {
    runTest {
      assertThat(
          runCatching {
              val actorProvider = FixedActorProvider(Actor.Unauthenticated)
              AuthenticationLock(actorProvider).scheduleUnlock {}
            }
            .exceptionOrNull()
        )
        .isNotNull()
        .isInstanceOf<AuthenticationLock.FailedAuthenticationException>()
        .prop(AuthenticationLock.FailedAuthenticationException::cause)
        .isNull()
    }
  }

  @Test
  fun createdAuthenticationLockUnlocksWithActorProvidedByTheSpecifiedProvider() {
    runTest {
      val actor = Actor.Authenticated("id", "access-token", TestImageLoader)
      val actorProvider = InMemoryActorProvider().apply { remember(actor) }
      AuthenticationLock(actorProvider).scheduleUnlock { assertThat(it).isSameAs(actor) }
    }
  }

  @Test
  fun createdAuthenticationLockUnlocksByAuthorizingThroughTheSpecifiedAuthorizer() {
    runTest {
      var hasAuthorized = false
      val authorizer = AuthorizerBuilder().before { hasAuthorized = true }.build()
      val actorProvider = InMemoryActorProvider()
      val authenticator =
        Authenticator(actorProvider, authorizer) {
          if (hasAuthorized) {
            Actor.Authenticated("id", "access-token", TestImageLoader)
          } else {
            Actor.Unauthenticated
          }
        }
      AuthenticationLock(authenticator, actorProvider).scheduleUnlock {}
      assertThat(hasAuthorized).isTrue()
    }
  }
}

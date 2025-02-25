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

package br.com.orcinus.orca.core.mastodon.instance.requester.authentication

import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.injector.Injector
import kotlin.test.Test

internal class AuthenticatedRequesterTests {
  @Test(expected = Injector.ModuleNotRegisteredException::class)
  fun throwsWhenCreatingAnAuthenticatedRequesterWithoutHavingRegisteredACoreModule() {
    lateinit var requester: Requester
    runRequesterTest { requester = this.requester }
    requester.authenticated()
  }

  @Test
  fun addsAuthorizationHeaderToDeleteRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.delete(route))
        .isEqualTo("Bearer ${Actor.Authenticated.sample.accessToken}")
    }
  }

  @Test
  fun addsAuthorizationHeaderToGetRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.get(route))
        .isEqualTo("Bearer ${Actor.Authenticated.sample.accessToken}")
    }
  }

  @Test
  fun addsAuthorizationHeaderToPostRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.post(route))
        .isEqualTo("Bearer ${Actor.Authenticated.sample.accessToken}")
    }
  }
}

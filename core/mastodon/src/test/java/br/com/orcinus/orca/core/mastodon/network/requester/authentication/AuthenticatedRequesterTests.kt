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

package br.com.orcinus.orca.core.mastodon.network.requester.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.mastodon.network.client.assertThatRequestAuthorizationHeaderOf
import br.com.orcinus.orca.core.mastodon.network.requester.Requester
import br.com.orcinus.orca.core.mastodon.network.requester.runRequesterTest
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
  fun schedulesAuthenticationOnDeleteRequest() {
    var hasAuthenticationBeenScheduled = false
    runAuthenticatedRequesterTest(onAuthentication = { hasAuthenticationBeenScheduled = true }) {
      requester.delete(route)
    }
    assertThat(hasAuthenticationBeenScheduled).isTrue()
  }

  @Test
  fun schedulesAuthenticationOnGetRequest() {
    var hasAuthenticationBeenScheduled = false
    runAuthenticatedRequesterTest(onAuthentication = { hasAuthenticationBeenScheduled = true }) {
      requester.get(route = route)
    }
    assertThat(hasAuthenticationBeenScheduled).isTrue()
  }

  @Test
  fun schedulesAuthenticationOnPostRequest() {
    var hasAuthenticationBeenScheduled = false
    runAuthenticatedRequesterTest(onAuthentication = { hasAuthenticationBeenScheduled = true }) {
      requester.post(route = route)
    }
    assertThat(hasAuthenticationBeenScheduled).isTrue()
  }

  @Test
  fun addsAuthorizationHeaderToDeleteRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.delete(route))
        .isEqualTo("Bearer test-access-token")
    }
  }

  @Test
  fun addsAuthorizationHeaderToGetRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.get(route = route))
        .isEqualTo("Bearer test-access-token")
    }
  }

  @Test
  fun addsAuthorizationHeaderToPostRequest() {
    runAuthenticatedRequesterTest {
      assertThatRequestAuthorizationHeaderOf(requester.post(route = route))
        .isEqualTo("Bearer test-access-token")
    }
  }
}

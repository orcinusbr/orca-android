/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.client

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.mastodon.client.test.assertThatRequestAuthorizationHeaderOf
import com.jeanbarrossilva.orca.core.mastodon.client.test.runAuthenticatedTest
import com.jeanbarrossilva.orca.core.mastodon.client.test.runUnauthenticatedTest
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import io.ktor.http.parametersOf
import kotlin.test.Test
import org.junit.Rule

internal class CoreHttpClientTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)

  @Test
  fun requestsAuthenticationOnAuthenticateAndGetWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndGet(route = "")
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndGetWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(client.authenticateAndGet(route = ""))
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndPostWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndPost(route = "")
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndPostWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(client.authenticateAndPost(route = ""))
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndSubmitFormWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndSubmitForm(route = "", parametersOf())
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndSubmitFormWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(
          client.authenticateAndSubmitForm(route = "", parametersOf())
        )
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }

  @Test
  fun requestsAuthenticationOnAuthenticateAndSubmitFormWithBinaryDataWithAnUnauthenticatedActor() {
    var isAuthenticated = false
    runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
      client.authenticateAndSubmitFormWithBinaryData(route = "", formData = emptyList())
      assertThat(isAuthenticated).isTrue()
    }
  }

  @Test
  fun setsBearerAuthHeaderOnAuthenticateAndSubmitFormWithBinaryDataWithAnAuthenticatedActor() {
    runAuthenticatedTest {
      assertThatRequestAuthorizationHeaderOf(
          client.authenticateAndSubmitFormWithBinaryData(route = "", formData = emptyList())
        )
        .isEqualTo("Bearer ${actor.accessToken}")
    }
  }
}

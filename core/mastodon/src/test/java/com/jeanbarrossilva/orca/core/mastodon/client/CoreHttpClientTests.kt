/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.mastodon.client

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.mastodon.client.test.assertThatRequestAuthorizationHeaderOf
import com.jeanbarrossilva.orca.core.mastodon.client.test.runAuthenticatedTest
import com.jeanbarrossilva.orca.core.mastodon.client.test.runUnauthenticatedTest
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import io.ktor.http.parametersOf
import kotlin.test.Test
import org.junit.Rule

internal class CoreHttpClientTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule()

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

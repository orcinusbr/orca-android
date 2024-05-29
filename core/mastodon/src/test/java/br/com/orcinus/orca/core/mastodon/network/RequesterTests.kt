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

package br.com.orcinus.orca.core.mastodon.network

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.mastodon.network.request.Authentication
import br.com.orcinus.orca.core.mastodon.network.request.Resumption
import br.com.orcinus.orca.core.mastodon.network.request.resumptionOf
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.http.Parameters
import kotlin.test.Test

internal class RequesterTests {
  @Test
  fun deletes() {
    runUnauthenticatedRequesterTest(onAuthentication = {}) {
      assertThat(it.delete(Authentication.None, "/api/v1/resource").body<String>()).isEqualTo("")
    }
  }

  @Test
  fun deletesSchedulingAuthentication() {
    var hasAuthenticated = false
    runUnauthenticatedRequesterTest(onAuthentication = { hasAuthenticated = true }) {
      it.delete(Authentication.Scheduled, "/api/v1/resource")
    }
    assertThat(hasAuthenticated).isTrue()
  }

  @Test
  fun retriesDeleteTwiceAfterFailure() {
    assertThat(retryCountOf { delete(Authentication.None, "/api/v1/resource") }).isEqualTo(2)
  }

  @Test
  fun gets() {
    runUnauthenticatedRequesterTest(onAuthentication = {}) {
      assertThat(it.get(Authentication.None, "/api/v1/resource").body<String>()).isEqualTo("")
    }
  }

  @Test
  fun getsSchedulingAuthentication() {
    var hasAuthenticated = false
    runUnauthenticatedRequesterTest(onAuthentication = { hasAuthenticated = true }) {
      it.get(Authentication.Scheduled, "/api/v1/resource")
    }
    assertThat(hasAuthenticated).isTrue()
  }

  @Test
  fun retriesGetTwiceAfterFailure() {
    assertThat(retryCountOf { get(Authentication.None, "/api/v1/resource") }).isEqualTo(2)
  }

  @Test
  fun posts() {
    runUnauthenticatedRequesterTest(onAuthentication = {}) {
      assertThat(it.post(Authentication.None, "/api/v1/resource").body<String>()).isEqualTo("")
    }
  }

  @Test
  fun postsSchedulingAuthentication() {
    var hasAuthenticated = false
    runUnauthenticatedRequesterTest(onAuthentication = { hasAuthenticated = true }) {
      it.get(Authentication.Scheduled, "/api/v1/resource")
    }
    assertThat(hasAuthenticated).isTrue()
  }

  @Test
  fun retriesPostTwiceAfterFailure() {
    assertThat(retryCountOf { post(Authentication.None, "/api/v1/resource") }).isEqualTo(2)
  }

  @Test
  fun doesNotResumeUnresumableDeleteRequestWhenItIsInterrupted() {
    assertThat(resumptionOf { delete(Authentication.None, "/api/v1/resource") })
      .isEqualTo(Resumption.None)
  }

  @Test
  fun resumesResumableDeleteRequestWhenItIsInterrupted() {
    assertThat(
        resumptionOf { delete(Authentication.None, "/api/v1/resource", Resumption.Resumable) }
      )
      .isEqualTo(Resumption.Resumable)
  }

  @Test
  fun doesNotResumeUnresumableGetRequestWhenItIsInterrupted() {
    assertThat(resumptionOf { get(Authentication.None, "/api/v1/resource") })
      .isEqualTo(Resumption.None)
  }

  @Test
  fun resumesResumableGetRequestWhenItIsInterrupted() {
    assertThat(resumptionOf { get(Authentication.None, "/api/v1/resource", Resumption.Resumable) })
      .isEqualTo(Resumption.Resumable)
  }

  @Test
  fun doesNotResumeUnresumablePostRequestWhenItIsInterrupted() {
    assertThat(resumptionOf { post(Authentication.None, "/api/v1/resource") })
      .isEqualTo(Resumption.None)
  }

  @Test
  fun resumesResumablePostRequestWithBodyParametersWhenItIsInterrupted() {
    assertThat(
        resumptionOf {
          post(Authentication.None, "/api/v1/resource", Parameters.Empty, Resumption.Resumable)
        }
      )
      .isEqualTo(Resumption.Resumable)
  }

  @Test
  fun resumesResumablePostRequestWithHeaderParametersWhenItIsInterrupted() {
    assertThat(
        resumptionOf {
          post(Authentication.None, "/api/v1/resource", formData(), Resumption.Resumable)
        }
      )
      .isEqualTo(Resumption.Resumable)
  }
}

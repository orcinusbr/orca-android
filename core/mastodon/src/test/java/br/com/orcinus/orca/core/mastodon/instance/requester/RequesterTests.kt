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

package br.com.orcinus.orca.core.mastodon.instance.requester

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.call.body
import kotlin.test.Test

internal class RequesterTests {
  @Test
  fun makesRouteAbsolute() {
    runRequesterTest {
      assertThat(requester.absolute(route))
        .isEqualTo(HostedURLBuilder.from(requester.baseURI).route())
    }
  }

  @Test
  fun deletes() {
    runRequesterTest { assertThat(requester.delete(route).body<String>()).isEqualTo("") }
  }

  @Test
  fun retriesDeleteTwiceAfterFailure() {
    assertThat(retryCountOf { requester.delete(route) }).isEqualTo(2)
  }

  @Test
  fun gets() {
    runRequesterTest { assertThat(requester.get(route).body<String>()).isEqualTo("") }
  }

  @Test
  fun retriesGetTwiceAfterFailure() {
    assertThat(retryCountOf { requester.get(route) }).isEqualTo(2)
  }

  @Test
  fun retriesPostTwiceAfterFailure() {
    assertThat(retryCountOf { requester.post(route) }).isEqualTo(2)
  }
}

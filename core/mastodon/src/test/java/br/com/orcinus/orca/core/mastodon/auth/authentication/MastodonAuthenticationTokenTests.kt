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

package br.com.orcinus.orca.core.mastodon.auth.authentication

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import java.net.URL
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class MastodonAuthenticationTokenTests {
  @Test
  fun convertsIntoActor() {
    val id = Actor.Authenticated.sample.id
    val accessToken = Actor.Authenticated.sample.accessToken
    val avatarURLAsString = "${Domain.sample.url}/accounts/$id/avatar.png"
    val verification = MastodonAuthenticationVerification(id, avatarURLAsString)
    val authToken = MastodonAuthenticationToken(accessToken)
    runTest {
      assertThat(authToken.toActor(AsyncImageLoader.Provider, verification)).all {
        prop(Actor.Authenticated::id).isEqualTo(id)
        prop(Actor.Authenticated::accessToken).isEqualTo(accessToken)
        transform { it.avatarLoader.source }.isEqualTo(URL(avatarURLAsString))
      }
    }
  }
}

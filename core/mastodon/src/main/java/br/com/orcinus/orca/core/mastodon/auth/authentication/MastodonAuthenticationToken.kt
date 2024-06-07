/*
 * Copyright © 2023-2024 Orcinus
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

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import java.net.URI
import kotlinx.serialization.Serializable

/**
 * Structure returned by the Mastodon API that holds the access token that's been given when
 * authorization was successfully granted to the user.
 *
 * @param accessToken Token that gives Orca user-level access to the API resources.
 */
@Serializable
internal data class MastodonAuthenticationToken(val accessToken: String) {
  /**
   * Converts this [MastodonAuthenticationToken] into an [authenticated][Actor.Authenticated]
   * [Actor].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   avatar will be loaded from a [URI].
   */
  suspend fun toActor(avatarLoaderProvider: SomeImageLoaderProvider<URI>): Actor.Authenticated {
    return toActor(
      avatarLoaderProvider,
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .requester
        .get({ path("api").path("v1").path("accounts").path("verify_credentials").build() }) {
          headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
        }
        .body<MastodonAuthenticationVerification>()
    )
  }

  /**
   * Converts this [MastodonAuthenticationToken] into an [authenticated][Actor.Authenticated]
   * [Actor].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   avatar will be loaded from a [URI].
   * @param verification Result of verifying the user's credentials.
   */
  fun toActor(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    verification: MastodonAuthenticationVerification
  ): Actor.Authenticated {
    return verification.toActor(avatarLoaderProvider, accessToken)
  }
}

/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the user's credentials are verified.
 *
 * @param id Unique identifier of the user.
 * @param avatar URL [String] that leads to the avatar image.
 */
@Serializable
internal data class MastodonAuthenticationVerification(val id: String, val avatar: String) {
  /**
   * Converts this [MastodonAuthenticationVerification] into an [authenticated][Actor.Authenticated]
   * [Actor].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   avatar will be loaded from a [URL].
   * @param accessToken Token that gives Orca user-level access to the API resources.
   */
  fun toActor(
    avatarLoaderProvider: SomeImageLoaderProvider<URL>,
    accessToken: String
  ): Actor.Authenticated {
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    return Actor.Authenticated(id, accessToken, avatarLoader)
  }
}

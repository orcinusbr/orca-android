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

package com.jeanbarrossilva.orca.core.auth.actor

import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoader

/** Agent that can perform operations throughout the application. */
sealed interface Actor {
  /** Unknown [Actor] that has restricted access. */
  object Unauthenticated : Actor

  /**
   * [Actor] that's been properly authenticated and can use the application normally.
   *
   * @param id Unique identifier within Mastodon.
   * @param accessToken Access token resulted from the authentication.
   * @param avatarLoader [ImageLoader] by which the avatar will be loaded.
   */
  data class Authenticated(
    val id: String,
    val accessToken: String,
    val avatarLoader: SomeImageLoader
  ) : Actor {
    companion object
  }
}

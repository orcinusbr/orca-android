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

package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL

/**
 * User that's authored a [Post].
 *
 * @param id Unique identifier.
 * @param avatarLoader [ImageLoader] that loads the avatar.
 * @param name Name to be displayed.
 * @param account Unique identifier within an instance.
 * @param profileURL [URL] that leads to this [Author]'s profile.
 */
data class Author(
  val id: String,
  val avatarLoader: SomeImageLoader,
  val name: String,
  val account: Account,
  val profileURL: URL
) : Serializable {
  companion object
}

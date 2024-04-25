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

package br.com.orcinus.orca.core.feed.profile.search

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoader
import java.io.Serializable
import java.net.URI

/**
 * Result of a profile search.
 *
 * @param id Unique identifier.
 * @param account Unique identifier within an instance.
 * @param avatarLoader [ImageLoader] that loads the avatar.
 * @param name Name to be displayed.
 * @param uri [URI] that leads to the profile.
 */
data class ProfileSearchResult
@InternalCoreApi
constructor(
  val id: String,
  val account: Account,
  val avatarLoader: SomeImageLoader,
  val name: String,
  val uri: URI
) : Serializable {
  companion object
}

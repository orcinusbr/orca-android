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

package com.jeanbarrossilva.orca.core.feed.profile.type.editable

import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/** Edits an [EditableProfile]. */
interface Editor {
  /**
   * Sets [avatarLoader] as the [EditableProfile]'s [avatarLoader][EditableProfile.avatarLoader].
   *
   * @param avatarLoader Avatar [ImageLoader] to be set to the [EditableProfile].
   */
  suspend fun setAvatarLoader(avatarLoader: SomeImageLoader)

  /**
   * Sets [name] as the [EditableProfile]'s [name][EditableProfile.name].
   *
   * @param name Name to be set to the [EditableProfile].
   */
  suspend fun setName(name: String)

  /**
   * Sets [bio] as the [EditableProfile]'s [bio][EditableProfile.bio].
   *
   * @param bio Bio to be set to the [EditableProfile].
   */
  suspend fun setBio(bio: StyledString)

  companion object {
    /** No-op, empty [Editor]. */
    val empty =
      object : Editor {
        override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {}

        override suspend fun setName(name: String) {}

        override suspend fun setBio(bio: StyledString) {}
      }
  }
}

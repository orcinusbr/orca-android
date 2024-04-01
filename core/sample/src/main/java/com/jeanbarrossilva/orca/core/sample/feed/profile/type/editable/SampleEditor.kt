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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.std.image.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * [Editor] that edits [SampleEditableProfile]s.
 *
 * @param profileWriter [SampleProfileWriter] for performing write operations on the [Profile].
 * @param id ID of the [Profile] to be edited.
 */
internal class SampleEditor(
  private val profileWriter: SampleProfileWriter,
  private val id: String
) : Editor {
  override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {
    edit { this.avatarLoader = avatarLoader }
  }

  override suspend fun setName(name: String) {
    edit { this.name = name }
  }

  override suspend fun setBio(bio: StyledString) {
    edit { this.bio = bio }
  }

  /**
   * Applies the [edit] to the [SampleEditableProfile] whose [ID][SampleEditableProfile.id] matches
   * [id].
   *
   * @param edit Editing to be made to the matching [SampleEditableProfile].
   */
  private suspend inline fun edit(crossinline edit: SampleEditableProfile.() -> Unit) {
    profileWriter.update(id) { (this as SampleEditableProfile).apply(edit) }
  }
}

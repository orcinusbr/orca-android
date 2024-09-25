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

package br.com.orcinus.orca.core.sample.feed.profile.type.editable

import br.com.orcinus.orca.core.feed.profile.type.editable.Editor
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import kotlin.reflect.KMutableProperty1

/**
 * [Editor] that edits [SampleEditableProfile]s.
 *
 * @param profileProvider [SampleProfileProvider] for performing write operations on the
 *   [SampleEditableProfile].
 * @param id ID of the [SampleEditableProfile] to be edited.
 * @see SampleEditableProfile.id
 */
internal class SampleEditor(
  private val profileProvider: SampleProfileProvider,
  private val id: String
) : Editor {
  override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {
    edit {
      @Suppress("UNCHECKED_CAST")
      (this::avatarLoader as KMutableProperty1<SampleEditableProfile, SomeImageLoader>).set(
        this,
        avatarLoader
      )
    }
  }

  override suspend fun setName(name: String) {
    edit {
      @Suppress("UNCHECKED_CAST")
      (this::name as KMutableProperty1<SampleEditableProfile, String>).set(this, name)
    }
  }

  override suspend fun setBio(bio: Markdown) {
    edit { this.bio = bio }
  }

  /**
   * Applies the [edit] to the [SampleEditableProfile] whose ID matches [id].
   *
   * @param edit Editing to be made to the matching [SampleEditableProfile].
   * @see SampleEditableProfile.id
   */
  private suspend inline fun edit(crossinline edit: SampleEditableProfile.() -> Unit) {
    profileProvider.update(id) { (this as SampleEditableProfile).apply(edit) }
  }
}

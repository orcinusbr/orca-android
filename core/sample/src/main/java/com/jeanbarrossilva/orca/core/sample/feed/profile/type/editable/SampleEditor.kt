/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
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

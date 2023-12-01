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

package com.jeanbarrossilva.orca.core.feed.profile.type.editable

import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
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

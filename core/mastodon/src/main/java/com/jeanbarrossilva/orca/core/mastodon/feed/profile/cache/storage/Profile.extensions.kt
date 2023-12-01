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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile

/**
 * Converts this [Profile] into a [MastodonProfileEntity].
 *
 * @throws IllegalStateException If no [MastodonProfileEntity] type has been mapped to this specific
 *   type of [Profile].
 * @see MastodonProfileEntity.Type
 */
internal fun Profile.toMastodonProfileEntity(): MastodonProfileEntity {
  return MastodonProfileEntity(
    id,
    "$account",
    avatarURL = "${avatarLoader.source}",
    name,
    "$bio",
    type =
      when (this) {
        is EditableProfile -> MastodonProfileEntity.EDITABLE_TYPE
        is FollowableProfile<*> -> MastodonProfileEntity.FOLLOWABLE_TYPE
        else -> throw IllegalStateException("No entity type for ${this::class.simpleName}.")
      },
    follow = if (this is FollowableProfile<*>) "$follow" else null,
    followerCount,
    followingCount,
    "$url"
  )
}

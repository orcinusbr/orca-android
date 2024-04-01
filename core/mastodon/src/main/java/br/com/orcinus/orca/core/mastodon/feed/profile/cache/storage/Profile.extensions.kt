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

package br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile

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

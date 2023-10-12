package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile

/**
 * Converts this [Profile] into an [HttpProfileEntity].
 *
 * @throws IllegalStateException If no [HttpProfileEntity] type has been mapped to this specific
 *   type of [Profile].
 * @see HttpProfileEntity.Type
 */
internal fun Profile.toMastodonProfileEntity(): HttpProfileEntity {
  return HttpProfileEntity(
    id,
    "$account",
    "$avatarURL",
    name,
    "$bio",
    type =
      when (this) {
        is EditableProfile -> HttpProfileEntity.EDITABLE_TYPE
        is FollowableProfile<*> -> HttpProfileEntity.FOLLOWABLE_TYPE
        else -> throw IllegalStateException("No entity type for ${this::class.simpleName}.")
      },
    follow = if (this is FollowableProfile<*>) "$follow" else null,
    followerCount,
    followingCount,
    "$url"
  )
}

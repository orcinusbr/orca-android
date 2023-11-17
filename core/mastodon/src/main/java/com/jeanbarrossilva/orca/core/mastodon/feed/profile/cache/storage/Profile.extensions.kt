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

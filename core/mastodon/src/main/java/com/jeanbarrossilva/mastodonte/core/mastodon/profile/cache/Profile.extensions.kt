package com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache

import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.persistence.entity.MastodonProfileEntity
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.type.followable.FollowableProfile

/**
 * Converts this [Profile] into a [MastodonProfileEntity].
 *
 * @throws IllegalStateException If no [MastodonProfileEntity] type has been mapped to this specific
 * type of [Profile].
 * @see MastodonProfileEntity.Type
 **/
internal fun Profile.toMastodonProfileEntity(): MastodonProfileEntity {
    return MastodonProfileEntity(
        id,
        "$account",
        "$avatarURL",
        name,
        bio,
        type = when (this) {
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

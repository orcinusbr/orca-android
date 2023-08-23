package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileEntity

/**
 * Converts this [Profile] into a [ProfileEntity].
 *
 * @throws IllegalStateException If no [ProfileEntity] type has been mapped to this specific
 * type of [Profile].
 * @see ProfileEntity.Type
 **/
internal fun Profile.toMastodonProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id,
        "$account",
        "$avatarURL",
        name,
        bio,
        type = when (this) {
            is EditableProfile -> ProfileEntity.EDITABLE_TYPE
            is FollowableProfile<*> -> ProfileEntity.FOLLOWABLE_TYPE
            else -> throw IllegalStateException("No entity type for ${this::class.simpleName}.")
        },
        follow = if (this is FollowableProfile<*>) "$follow" else null,
        followerCount,
        followingCount,
        "$url"
    )
}

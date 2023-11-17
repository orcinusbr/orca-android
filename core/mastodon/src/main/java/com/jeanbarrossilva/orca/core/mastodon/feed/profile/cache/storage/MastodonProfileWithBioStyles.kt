package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity

/**
 * Relationship between the [profile] and the [styles] applied to its
 * [bio][MastodonProfileEntity.bio].
 *
 * @param profile [MastodonProfileEntity] whose [bio][MastodonProfileEntity.bio] has the [styles]
 *   are applied.
 * @param styles [Mastodon style entities][MastodonStyleEntity] that belong to the [profile].
 */
internal data class MastodonProfileWithBioStyles(
  @Embedded val profile: MastodonProfileEntity,
  @Relation(parentColumn = "id", entityColumn = "parent_id") val styles: List<MastodonStyleEntity>
)

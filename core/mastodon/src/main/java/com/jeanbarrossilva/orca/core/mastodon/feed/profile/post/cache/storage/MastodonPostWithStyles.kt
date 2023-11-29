package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity

/**
 * Relationship between the [post] and the [styles] applied to it.
 *
 * @param post [MastodonPostEntity] to which the [styles] are applied.
 * @param styles [Mastodon style entities][MastodonStyleEntity] that belong to the [post].
 */
internal data class MastodonPostWithStyles(
  @Embedded val post: MastodonPostEntity,
  @Relation(parentColumn = "id", entityColumn = "parent_id") val styles: List<MastodonStyleEntity>
)

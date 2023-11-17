package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity

/**
 * Relationship between the [toot] and the [styles] applied to it.
 *
 * @param toot [MastodonTootEntity] to which the [styles] are applied.
 * @param styles [Mastodon style entities][MastodonStyleEntity] that belong to the [toot].
 */
internal data class MastodonTootWithStyles(
  @Embedded val toot: MastodonTootEntity,
  @Relation(parentColumn = "id", entityColumn = "parent_id") val styles: List<MastodonStyleEntity>
)

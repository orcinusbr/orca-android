package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention.MentionEntity

internal data class MastodonTootWithMentions(
    @Embedded val toot: MastodonTootEntity,
    @Relation(parentColumn = "id", entityColumn = "toot_id") val mentions: List<MentionEntity>
)

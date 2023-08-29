package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.Mention

/**
 * Converts this [Mention] into a [MentionEntity].
 *
 * @param tootID ID of the [Toot] to which this [Mention] belongs.
 **/
internal fun Mention.toMentionEntity(tootID: String): MentionEntity {
    return MentionEntity(id = 0, tootID, indices.first, indices.last, "$url")
}

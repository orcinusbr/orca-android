package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention

/**
 * Gets the [MentionEntity] that starts at the given [index].
 *
 * @param index Index at which the [MentionEntity] to be obtained starts.
 **/
internal fun Collection<MentionEntity>.startingAt(index: Int): MentionEntity {
    return first {
        it.startIndex == index
    }
}

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.style

/**
 * Gets the [StyleEntity] that starts at the given [index].
 *
 * @param index Index at which the [StyleEntity] to be obtained starts.
 **/
internal fun Collection<StyleEntity>.startingAt(index: Int): StyleEntity {
    return first {
        it.startIndex == index
    }
}

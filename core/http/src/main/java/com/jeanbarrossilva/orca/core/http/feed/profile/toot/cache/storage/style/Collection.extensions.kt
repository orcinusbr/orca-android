package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style

/**
 * Gets the [HttpStyleEntity] that starts at the given [index].
 *
 * @param index Index at which the [HttpStyleEntity] to be obtained starts.
 */
internal fun Collection<HttpStyleEntity>.startingAt(index: Int): HttpStyleEntity {
  return first { it.startIndex == index }
}

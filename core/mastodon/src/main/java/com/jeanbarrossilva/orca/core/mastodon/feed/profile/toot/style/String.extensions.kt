package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style

/**
 * Trims indents of all lengths.
 *
 * @return This [String] without indents.
 **/
internal fun String.trimIndents(): String {
    return lines().joinToString(separator = "\n", transform = String::trimStart)
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style

/**
 * Builds a [StyledString].
 *
 * @param build Configures the [StyledString] to be built.
 **/
inline fun buildStyledString(build: StyledString.Builder.() -> Unit): StyledString {
    return StyledString.Builder().apply(build).build()
}

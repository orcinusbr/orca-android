package com.jeanbarrossilva.orca.std.styledstring

/**
 * Builds a [StyledString].
 *
 * @param build Configures the [StyledString] to be built.
 **/
inline fun buildStyledString(build: StyledString.Builder.() -> Unit): StyledString {
    return StyledString.Builder().apply(build).build()
}

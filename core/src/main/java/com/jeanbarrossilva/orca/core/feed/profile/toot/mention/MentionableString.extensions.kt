package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

/**
 * Builds a [MentionableString].
 *
 * @param build Configures the [MentionableString] to be built.
 **/
inline fun buildMentionableString(build: MentionableString.Builder.() -> Unit): MentionableString {
    return MentionableString.Builder().apply(build).build()
}

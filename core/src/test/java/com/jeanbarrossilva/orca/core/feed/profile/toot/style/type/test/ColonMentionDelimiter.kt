package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention

internal object ColonMentionDelimiter : Mention.Delimiter.Child() {
    override fun getRegex(): Regex {
        return Regex(":[a-zA-Z0-9._%+-]+")
    }

    override fun onGetTarget(match: String): String {
        return match.removePrefix(":")
    }

    override fun onTarget(target: String): String {
        return ":$target"
    }
}

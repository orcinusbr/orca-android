package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

internal object ColonMentionStyle : Style() {
    override fun getRegex(): Regex {
        return Regex(":[a-zA-Z0-9._%+-]+")
    }

    override fun onGetTarget(match: String): String {
        return match.removePrefix(":")
    }
}

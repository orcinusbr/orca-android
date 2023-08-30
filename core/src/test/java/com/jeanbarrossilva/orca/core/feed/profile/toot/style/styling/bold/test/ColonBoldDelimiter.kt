package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold.Bold

internal object ColonBoldDelimiter : Bold.Delimiter.Child() {
    override val regex = Regex(":(.*?):")

    override fun onGetTarget(match: String): String {
        return match.substringAfter(':').substringBeforeLast(':')
    }

    override fun onTarget(target: String): String {
        return ":$target:"
    }
}

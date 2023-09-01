package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold

internal object ColonBoldDelimiter : Bold.Delimiter.Child() {
    private val delegate = surroundedBy(':')

    override fun getRegex(): Regex {
        return delegate.regex
    }

    override fun onGetTarget(match: String): String {
        return delegate.getTarget(match)
    }

    override fun onTarget(target: String): String {
        return delegate.target(target)
    }
}

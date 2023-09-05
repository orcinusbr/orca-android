package com.jeanbarrossilva.orca.std.styledstring.type.test.mention

import com.jeanbarrossilva.orca.std.styledstring.type.Mention

internal object ColonMentionDelimiter : Mention.Delimiter.Variant() {
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

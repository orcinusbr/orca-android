package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.URLFinder
import java.net.URL

internal class MastodonMentionDelimiter(status: Status) : Mention.Delimiter.Child() {
    private val urlFinder = URLFinder(status.content)

    public override val regex = Regex(tag("${Link.regex}", username = "[a-zA-Z0-9._%+-]+"))

    override fun onGetTarget(match: String): String {
        return match.substringAfter(TARGET_IMMEDIATE_PREFIX).substringBefore(
            TARGET_IMMEDIATE_SUFFIX
        )
    }

    override fun onTarget(target: String): String {
        val url = urlFinder.find(target).toString()
        return tag(url, target)
    }

    fun getNextURL(): URL? {
        return urlFinder.next()
    }

    companion object {
        private const val TARGET_IMMEDIATE_PREFIX = "@<span>"
        private const val TARGET_IMMEDIATE_SUFFIX = "</span>"

        fun tag(url: String, username: String): String {
            return "<a href=\"$url\" class=\"u-url mention\">$TARGET_IMMEDIATE_PREFIX$username" +
                "$TARGET_IMMEDIATE_SUFFIX</a>"
        }
    }
}

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.URLFinder

internal class MastodonHashtagDelimiter(status: Status) : Hashtag.Delimiter.Child() {
    private val urlFinder = URLFinder(status.content)

    public override val regex = Regex(tag(url = "${Link.regex}", target = "${Hashtag.targetRegex}"))

    override fun onGetTarget(match: String): String {
        return match.substringAfter(TARGET_IMMEDIATE_PREFIX).substringBefore(
            TARGET_IMMEDIATE_SUFFIX
        )
    }

    override fun onTarget(target: String): String {
        val url = urlFinder.find(target).toString()
        return tag(url, target)
    }

    companion object {
        private const val TARGET_IMMEDIATE_PREFIX = "#<span>"
        private const val TARGET_IMMEDIATE_SUFFIX = "</span>"

        fun tag(url: String, target: String): String {
            return "<a href=\"$url\" class=\"mention hashtag\" rel=\"tag\">" +
                "$TARGET_IMMEDIATE_PREFIX$target$TARGET_IMMEDIATE_SUFFIX</a>"
        }
    }
}

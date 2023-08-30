package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.isMention
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class MastodonMentionDelimiter(status: Status) : Mention.Delimiter.Child() {
    private val urls = Jsoup
        .parse(status.content, "${Mastodon.baseUrl}")
        .allElements
        .filter(predicate = Element::isMention)
        .map { it.attr("abs:href") }
        .map { it.removeSuffix("@${Mastodon.INSTANCE}") }
        .map(::URL)
    private val urlIterator = urls.iterator()

    public override val regex = Regex(
        "<a href=\"${Link.regex}\" class=\"u-url mention\">$TARGET_IMMEDIATE_PREFIX[a-zA-Z0-9._%+" +
            "-]+$TARGET_IMMEDIATE_SUFFIX</a>"
    )

    override fun onGetTarget(match: String): String {
        return match.substringAfter(TARGET_IMMEDIATE_PREFIX).substringBefore(
            TARGET_IMMEDIATE_SUFFIX
        )
    }

    override fun onTarget(target: String): String {
        val url = urls.map(URL::toString).first { target in it }
        return tag(url, target)
    }

    fun getNextURL(): URL? {
        return if (urlIterator.hasNext()) {
            urlIterator.next()
        } else {
            null
        }
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

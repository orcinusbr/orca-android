package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.MentionDelimiter
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.isMention
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class MastodonMentionDelimiter(status: Status) : MentionDelimiter.Child() {
    private val urls = Jsoup
        .parse(status.content, "${Mastodon.baseUrl}")
        .allElements
        .filter(predicate = Element::isMention)
        .map { it.attr("abs:href") }
        .map { it.removeSuffix("@${Mastodon.INSTANCE}") }
        .map(::URL)
    private val urlIterator = urls.iterator()

    public override val regex = Regex(
        "<a href=\"https?://(?:www.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z" +
            "0-9()@:%_+.~#?&/=]*\" class=\"u-url mention\">$TARGET_IMMEDIATE_PREFIX[a-zA-Z0-9._%+" +
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

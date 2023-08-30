package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.isMention
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class MastodonMentionDelimiter(status: Status) : Style.Delimiter() {
    private val urls = Jsoup
        .parse(status.content, "${Mastodon.baseUrl}")
        .allElements
        .filter(predicate = Element::isMention)
        .map { it.attr("abs:href") }
        .map { it.removeSuffix("@${Mastodon.INSTANCE}") }
        .map(::URL)
        .iterator()

    public override fun getRegex(): Regex {
        return Regex(
            "<a href=\"https?://(?:www.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-" +
                "zA-Z0-9()@:%_+.~#?&/=]*\" class=\"u-url mention\">$START[a-zA-Z0-9._%+-]+$END</a>"
        )
    }

    override fun onGetTarget(match: String): String {
        return match.substringAfter(START).substringBefore(END)
    }

    fun getNextURL(): URL? {
        return if (urls.hasNext()) {
            urls.next()
        } else {
            null
        }
    }

    companion object {
        private const val START = "@<span>"
        private const val END = "</span>"

        fun tag(url: String, username: String): String {
            return "<a href=\"$url\" class=\"u-url mention\">$START$username$END</a>"
        }
    }
}

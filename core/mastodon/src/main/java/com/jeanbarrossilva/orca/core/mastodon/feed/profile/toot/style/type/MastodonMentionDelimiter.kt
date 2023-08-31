package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.URLFinder
import java.net.URL
import kotlinx.html.a
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import org.jsoup.Jsoup

internal class MastodonMentionDelimiter(status: Status) : Mention.Delimiter.Child() {
    private val urlFinder = URLFinder(status.content)

    public override val regex = Regex(tag("${Link.regex}", username = "[a-zA-Z0-9._%+-]+"))

    override fun onGetTarget(match: String): String {
        return Jsoup
            .parse(match)
            .select("span")
            .first()
            ?.selectFirst("a")
            ?.selectFirst("span")
            ?.text()
            ?: throw TargetNotFoundException(match)
    }

    override fun onTarget(target: String): String {
        val url = urlFinder.find(target).toString()
        return tag(url, target)
    }

    fun getNextURL(): URL? {
        return urlFinder.next()
    }

    companion object {
        fun tag(url: String, username: String): String {
            return createHTML().span(classes = "h-card") {
                translate(false)
                a(href = url, classes = "u-url mention") {
                    +"@"
                    span { +username }
                }
            }
        }
    }
}

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.URLFinder
import kotlinx.html.a
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import org.jsoup.Jsoup

internal class MastodonHashtagDelimiter(status: Status) : Hashtag.Delimiter.Variant() {
    private val urlFinder = URLFinder(status.content)

    public override fun getRegex(): Regex {
        return Regex(tag(url = "${Link.protocoledRegex}", target = "${Hashtag.targetRegex}"))
    }

    override fun onGetTarget(match: String): String {
        return Jsoup.parse(match).selectFirst("span")?.text() ?: throw TargetNotFoundException(
            match
        )
    }

    override fun onTarget(target: String): String {
        val url = urlFinder.find(target).toString()
        return tag(url, target)
    }

    companion object {
        fun tag(url: String, target: String): String {
            return createHTML().a(href = url, classes = "mention hashtag") {
                rel = "tag"
                +"#"
                span { +target }
            }
        }
    }
}

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import java.net.URL
import kotlinx.html.A
import kotlinx.html.a
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import org.jsoup.Jsoup

internal object MastodonLinkDelimiter : Link.Delimiter.Child() {
    private const val VISIBLE_UNPROTOCOLED_URL_THRESHOLD = 30

    public override fun getRegex(): Regex {
        return Regex(
            tag(href = "${Link.protocoledRegex}") {
                span(classes = "invisible") { +"${Link.protocolRegex}(?:${Link.subdomainRegex})?" }
                span(classes = "(ellipsis)?") { +Link.unprotocoledRegex.toString() }
                span(classes = "invisible") { +"(${Link.unprotocoledRegex}|${Link.pathRegex})?" }
            }
        )
    }

    override fun onGetTarget(match: String): String {
        return Jsoup.parse(match).selectFirst("a")?.wholeText() ?: throw TargetNotFoundException(
            match
        )
    }

    override fun onTarget(target: String): String {
        val url = URL(target)
        return tag(url)
    }

    fun tag(url: URL): String {
        val separatedProtocol = "${url.protocol}://"
        val unprotocoledURLAsString = url.toString().removePrefix(separatedProtocol)
        val hasTrailingInvisibleSpan =
            unprotocoledURLAsString.length > VISIBLE_UNPROTOCOLED_URL_THRESHOLD
        return tag(href = "$url") {
            span(classes = "invisible") { +separatedProtocol }
            span(classes = "ellipsis") {
                +unprotocoledURLAsString.take(VISIBLE_UNPROTOCOLED_URL_THRESHOLD)
            }
            if (hasTrailingInvisibleSpan) {
                span(classes = "invisible") {
                    +unprotocoledURLAsString.substring(
                        VISIBLE_UNPROTOCOLED_URL_THRESHOLD..unprotocoledURLAsString.length.dec()
                    )
                }
            }
        }
    }

    private fun tag(href: String, tag: A.() -> Unit): String {
        return createHTML(prettyPrint = false).a(href, target = "_blank") {
            rel = "nofollow noopener noreferrer"
            translate = false
            tag()
        }
    }
}

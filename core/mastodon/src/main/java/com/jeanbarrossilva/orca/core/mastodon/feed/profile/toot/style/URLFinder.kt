package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class URLFinder(html: String) {
    private val urls = Jsoup
        .parse(html, "${Mastodon.baseUrl}")
        .allElements
        .filter(predicate = Element::isMention)
        .map { it.attr("abs:href") }
        .map { it.removeSuffix("@${Mastodon.INSTANCE}") }
        .map(::URL)

    val iterator = urls.iterator()

    @Throws(NoSuchElementException::class)
    fun find(query: String): URL {
        return urls.first {
            query in it.toString()
        }
    }

    fun next(): URL? {
        return if (iterator.hasNext()) {
            iterator.next()
        } else {
            null
        }
    }
}

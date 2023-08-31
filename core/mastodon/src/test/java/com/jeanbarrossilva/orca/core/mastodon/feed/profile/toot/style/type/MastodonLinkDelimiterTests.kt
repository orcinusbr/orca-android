package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import java.net.URL
import kotlinx.html.a
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class MastodonLinkDelimiterTests {
    @Test
    fun `GIVEN a URL that exceeds the threshold WHEN tagging it THEN it ends with an invisible span`() { // ktlint-disable max-line-length
        assertEquals(
            createHTML(prettyPrint = false).a(
                href = "https://nsscreencast.com/series/61-macos-mastodon-client",
                target = "_blank"
            ) {
                rel = "nofollow noopener noreferrer"
                translate = false
                span(classes = "invisible") { +"https://" }
                span(classes = "ellipsis") { +"nsscreencast.com/series/61-mac" }
                span(classes = "invisible") { +"os-mastodon-client" }
            },
            MastodonLinkDelimiter
                .tag(URL("https://nsscreencast.com/series/61-macos-mastodon-client"))
        )
    }

    @Test
    fun `GIVEN a URL that exceeds the threshold WHEN checking if it's a link THEN it is`() {
        assertTrue(
            createHTML(prettyPrint = false).a(
                href = "https://nsscreencast.com/series/61-macos-mastodon-client",
                target = "_blank"
            ) {
                rel = "nofollow noopener noreferrer"
                translate = false
                span(classes = "invisible") { +"https://" }
                span(classes = "ellipsis") { +"nsscreencast.com/series/61-mac" }
                span(classes = "invisible") { +"os-mastodon-client" }
            }
                .matches(MastodonLinkDelimiter.regex)
        )
    }
}

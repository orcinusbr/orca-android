package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.pagination.links
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import org.junit.Assert.assertEquals
import org.junit.Test

internal class HeadersExtensionsTests {
    @Test
    fun `GIVEN String Link headers WHEN converting each into a LinkHeader THEN they're converted`() { // ktlint-disable max-line-length
        val headers = headers {
            append(HttpHeaders.Link, """<https://example.com/next>; rel="next"""")
            append(HttpHeaders.Link, """<https://example.com/previous>; rel="previous"""")
        }
            .links
        assertEquals("https://example.com/next", headers.first().uri)
        assertEquals("https://example.com/previous", headers[1].uri)
        assertEquals("next", headers.first().parameter("rel"))
        assertEquals("previous", headers[1].parameter("rel"))
    }
}

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.test.sample
import org.junit.Assert.assertTrue
import org.junit.Test

internal class MastodonHashtagDelimiterTests {
    @Test
    fun `GIVEN a target surrounded by hashtag tags WHEN checking if it's a hashtag THEN it is`() {
        assertTrue(
            MastodonHashtagDelimiter.tag("https://pudim.com.br", "pudim").matches(
                MastodonHashtagDelimiter(Status.sample).regex
            )
        )
    }
}

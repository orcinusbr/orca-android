package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.mention.MastodonMentionDelimiter
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.test.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class MastodonMentionDelimiterTests {
    @Test
    fun `GIVEN an account surrounded by tags WHEN checking if it's a mention THEN it is`() { // ktlint-disable max-line-length
        assertTrue(
            MastodonMentionDelimiter
                .tag("${Profile.sample.url}", Profile.sample.account.username)
                .matches(MastodonMentionDelimiter(Status.sample).getRegex())
        )
    }

    @Test
    fun `GIVEN an account surrounded by tags WHEN getting its URL THEN it's obtained`() {
        assertEquals(
            Profile.sample.url,
            MastodonMentionDelimiter(
                Status.sample.copy(
                    content = MastodonMentionDelimiter.tag(
                        "${Profile.sample.url}",
                        Profile.sample.account.username
                    )
                )
            )
                .getNextURL()
        )
    }
}

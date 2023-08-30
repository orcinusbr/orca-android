package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.styling.mention.MastodonMentionStyle
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.test.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class MastodonMentionStyleTests {
    @Test
    fun `GIVEN an account surrounded by tags WHEN checking if it's a mention THEN it is`() { // ktlint-disable max-line-length
        assertTrue(
            MastodonMentionStyle
                .tag("${Profile.sample.url}", Profile.sample.account.username)
                .matches(MastodonMentionStyle(Status.sample).getRegex())
        )
    }

    @Test
    fun `GIVEN an account surrounded by tags WHEN getting its URL THEN it's obtained`() {
        assertEquals(
            Profile.sample.url,
            MastodonMentionStyle(
                Status.sample.copy(
                    content = MastodonMentionStyle.tag(
                        "${Profile.sample.url}",
                        Profile.sample.account.username
                    )
                )
            )
                .getNextURL()
        )
    }
}

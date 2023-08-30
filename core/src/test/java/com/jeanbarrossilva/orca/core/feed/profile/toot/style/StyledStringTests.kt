package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.test.ColonMentionStyle
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class StyledStringTests {
    @Test
    fun `GIVEN a string with mentions delimited differently WHEN normalizing it THEN they're delimited by the mention symbol`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                mention(Account.sample.username, Profile.sample.url)
                append(", ")
                mention("_inside", URL("https://mastodon.social/@_inside"))
                append(", hello!")
            }
                .toString(),
            StyledString.normalize(
                ":${Account.sample.username}, :_inside, hello!",
                ColonMentionStyle
            )
        )
    }

    @Test
    fun `GIVEN a mention WHEN appending it THEN it's been placed correctly`() {
        assertEquals(
            Mention(indices = 7..(7 + Account.sample.username.length), Profile.sample.url),
            buildStyledString {
                append("Hello, ")
                mention(Account.sample.username, Profile.sample.url)
                append("!")
            }
                .mentions
                .single()
        )
    }

    @Test
    fun `GIVEN a styled string WHEN getting its string representation THEN it has the mentions`() { // ktlint-disable max-line-length
        assertEquals(
            "Olá, @jeanbarrossilva!",
            buildStyledString {
                append("Olá, ")
                mention(Account.sample.username, Profile.sample.url)
                append("!")
            }
                .toString()
        )
    }

    @Test
    fun `GIVEN a mention put directly in the underlying string WHEN getting the mentions THEN it's not found`() { // ktlint-disable max-line-length
        assertContentEquals(
            emptyList(),
            StyledString("안녕하세요, " + Mention.SYMBOL + Account.sample.username + '!')
                .mentions
        )
    }
}

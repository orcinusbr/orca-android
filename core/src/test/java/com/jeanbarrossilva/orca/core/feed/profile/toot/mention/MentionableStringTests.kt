package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.mention.test.ColonMentionDelimiter
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class MentionableStringTests {
    @Test
    fun `GIVEN a string with mentions delimited differently WHEN normalizing it THEN they're delimited by the mention symbol`() { // ktlint-disable max-line-length
        assertEquals(
            buildMentionableString {
                mention(Account.sample.username, Profile.sample.url)
                append(", ")
                mention("_inside", URL("https://mastodon.social/@_inside"))
                append(", hello!")
            }
                .toString(),
            MentionableString.normalize(
                ":${Account.sample.username}, :_inside, hello!",
                ColonMentionDelimiter
            )
        )
    }

    @Test
    fun `GIVEN a mention WHEN appending it THEN it's been placed correctly`() {
        assertEquals(
            Mention(indices = 7..(7 + Account.sample.username.length), Profile.sample.url),
            buildMentionableString {
                append("Hello, ")
                mention(Account.sample.username, Profile.sample.url)
                append("!")
            }
                .mentions
                .single()
        )
    }

    @Test
    fun `GIVEN a mentionable string WHEN getting its string representation THEN it has the mentions`() { // ktlint-disable max-line-length
        assertEquals(
            "Olá, @jeanbarrossilva!",
            buildMentionableString {
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
            MentionableString("안녕하세요, " + Mention.SYMBOL + Account.sample.username + '!')
                .mentions
        )
    }
}

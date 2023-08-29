package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.mention.test.ColonMentionDelimiter
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StringExtensionsTests {
    @Test
    fun `GIVEN a string with a mention WHEN converting it into a mentionable string THEN the mention is preserved`() { // ktlint-disable max-line-length
        assertEquals(
            buildMentionableString {
                append("Hello, ")
                mention(Account.sample.username, Profile.sample.url)
                append('!')
            },
            "Hello, @${Account.sample.username}!".toMentionableString { Profile.sample.url }
        )
    }

    @Test
    fun `GIVEN a string with a mention containing two delimiters WHEN converting it into a mentionable string THEN the leading delimiter is ignored`() { // ktlint-disable max-line-length
        assertEquals(
            buildMentionableString {
                append("Hello, @")
                mention(Account.sample.username, Profile.sample.url)
                append('!')
            },
            "Hello, @@${Account.sample.username}!".toMentionableString { Profile.sample.url }
        )
    }

    @Test
    fun `GIVEN a string with multiple mentions WHEN converting it into a mentionable string THEN the mentions are preserved`() { // ktlint-disable max-line-length
        assertEquals(
            buildMentionableString {
                append("Hello, ")
                mention(Account.sample.username, Profile.sample.url)
                append(" and ")
                mention("christianselig", URL("https://mastodon.social/@christianselig"))
                append('!')
            },
            "Hello, @${Account.sample.username} and @christianselig!".toMentionableString {
                when (it) {
                    7 -> Profile.sample.url
                    28 -> URL("https://mastodon.social/@christianselig")
                    else -> throw IllegalStateException("🫠")
                }
            }
        )
    }

    @Test
    fun `GIVEN a string with a different mention delimiter WHEN converting it into a mentionable string THEN the mentions start with their default symbol`() { // ktlint-disable max-line-length
        assertEquals(
            "Hello, @${Account.sample.username}!",
            "Hello, :${Account.sample.username}!"
                .toMentionableString(ColonMentionDelimiter)
                .toString()
        )
    }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.test.ColonMentionDelimiter
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StringExtensionsTests {
    @Test
    fun `GIVEN a string with a mention WHEN converting it into a styled string THEN the mention is preserved`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                +"Hello, "
                mention(Profile.sample.url) { +Account.sample.username }
                +('!')
            },
            "Hello, @${Account.sample.username}!".toStyledString { Profile.sample.url }
        )
    }

    @Test
    fun `GIVEN a string with a mention containing two delimiters WHEN converting it into a styled string THEN the leading delimiter is ignored`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                +"Hello, ${Mention.SYMBOL}"
                mention(Profile.sample.url) { +Account.sample.username }
                +'!'
            },
            ("Hello, " + Mention.SYMBOL + Mention.SYMBOL + Account.sample.username + '!')
                .toStyledString { Profile.sample.url }
        )
    }

    @Test
    fun `GIVEN a string with multiple mentions WHEN converting it into a styled string THEN the mentions are preserved`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                +"Hello, "
                mention(Profile.sample.url) { +Account.sample.username }
                +" and "
                mention(URL("https://mastodon.social/@christianselig")) { +"christianselig" }
                +'!'
            },
            "Hello, @${Account.sample.username} and @christianselig!".toStyledString {
                when (it) {
                    7 -> Profile.sample.url
                    28 -> URL("https://mastodon.social/@christianselig")
                    else -> throw IllegalStateException("🫠")
                }
            }
        )
    }

    @Test
    fun `GIVEN a string with a different mention delimiter WHEN converting it into a styled string THEN the mentions start with their default symbol`() { // ktlint-disable max-line-length
        assertEquals(
            "Hello, " + Mention.SYMBOL + Account.sample.username + '!',
            "Hello, :${Account.sample.username}!"
                .toStyledString(mentionDelimiter = ColonMentionDelimiter)
                .toString()
        )
    }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.test.ColonBoldDelimiter
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.test.ColonMentionDelimiter
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class StyledStringTests {
    @Test
    fun `GIVEN a string with bold portions delimited differently WHEN normalizing it THEN they're delimited by the bold symbol`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                embolden("Hi")
                append(", ")
                embolden("hello")
                append('!')
            }
                .toString(),
            StyledString.normalize(":Hi:, :hello:!", ColonBoldDelimiter)
        )
    }

    @Test
    fun `GIVEN an invalid e-mail WHEN appending it THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            buildStyledString {
                email("john@@appleseed.com")
            }
        }
    }

    @Test
    fun `GIVEN a string with an e-mail WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                append("Send a message to ")
                email("john@appleseed.com")
                append('!')
            },
            "Send a message to john@appleseed.com!".toStyledString()
        )
    }

    @Test
    fun `GIVEN an invalid subject WHEN appending a hashtag THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            buildStyledString {
                hashtag("subjects - cannot - have - whitespaces")
            }
        }
    }

    @Test
    fun `GIVEN a string with a link WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                append("Check out ")
                linkTo(URL("https://pudim.com.br"))
                append('!')
            },
            "Check out https://pudim.com.br!".toStyledString()
        )
    }

    @Test
    fun `GIVEN a string with multiple links WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                append("Check both ")
                linkTo(URL("https://rambo.codes"))
                append(" and ")
                linkTo(URL("https://hackingwithswift.com"))
                append(" out!")
            },
            "Check both https://rambo.codes and https://hackingwithswift.com out!".toStyledString()
        )
    }

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
                ColonMentionDelimiter
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
                .styles
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
                .styles
        )
    }
}

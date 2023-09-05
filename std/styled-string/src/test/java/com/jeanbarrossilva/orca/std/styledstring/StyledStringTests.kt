package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.type.Mention
import com.jeanbarrossilva.orca.std.styledstring.type.test.ColonBoldDelimiter
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.ColonMentionDelimiter
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.url
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.username
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
                bold { +"Hi" }
                +", "
                bold { +"hello" }
                +'!'
            }
                .toString(),
            StyledString.normalize(":Hi:, :hello:!", ColonBoldDelimiter)
        )
    }

    @Test
    fun `GIVEN an invalid e-mail WHEN appending it THEN it isn't stylized as an e-mail`() {
        assertContentEquals(buildStyledString { +"john@@appleseed.com" }.styles, emptyList())
    }

    @Test
    fun `GIVEN a string with an e-mail WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString { +"Send a message to john@appleseed.com!" },
            "Send a message to john@appleseed.com!".toStyledString()
        )
    }

    @Test
    fun `GIVEN an invalid subject WHEN appending a hashtag THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            buildStyledString {
                hashtag {
                    +"subjects - cannot - have - whitespaces"
                }
            }
        }
    }

    @Test
    fun `GIVEN a string with italicized portions WHEN converting it into a styled string THEN the italic style is preserved`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                +"Hello, "
                italic { +"world" }
                +'!'
            },
            ("Hello, " + Italic.SYMBOL + "world" + Italic.SYMBOL + '!').toStyledString()
        )
    }

    @Test
    fun `GIVEN a string with a link WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString { +"Check out https://pudim.com.br!" },
            "Check out https://pudim.com.br!".toStyledString()
        )
    }

    @Test
    fun `GIVEN a string with multiple links WHEN converting it into a styled string THEN it's styled accordingly`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                +"Check both https://rambo.codes and https://hackingwithswift.com out!"
            },
            "Check both https://rambo.codes and https://hackingwithswift.com out!".toStyledString()
        )
    }

    @Test
    fun `GIVEN a string with mentions delimited differently WHEN normalizing it THEN they're delimited by the mention symbol`() { // ktlint-disable max-line-length
        assertEquals(
            buildStyledString {
                mention(Mention.url) { +Mention.username }
                +", "
                mention(URL("https://mastodon.social/@_inside")) { +"_inside" }
                +", hello!"
            }
                .toString(),
            StyledString
                .normalize(":${Mention.username}, :_inside, hello!", ColonMentionDelimiter)
        )
    }

    @Test
    fun `GIVEN a mention WHEN appending it THEN it's been placed correctly`() {
        assertEquals(
            Mention(indices = 7..(7 + Mention.username.length), Mention.url),
            buildStyledString {
                +"Hello, "
                mention(Mention.url) { +Mention.username }
                +"!"
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
                +"Olá, "
                mention(Mention.url) { +Mention.username }
                +"!"
            }
                .toString()
        )
    }

    @Test
    fun `GIVEN a mention put directly in the underlying string WHEN getting the mentions THEN it's not found`() { // ktlint-disable max-line-length
        assertContentEquals(
            emptyList(),
            StyledString("안녕하세요, " + Mention.SYMBOL + Mention.username + '!')
                .styles
        )
    }

    @Test
    fun `GIVEN nested styles WHEN appending text with them THEN they've been applied`() {
        assertContentEquals(
            listOf(Bold(0..6), Italic(0..6)),
            buildStyledString {
                bold {
                    italic {
                        +"Hello"
                    }
                }
                +'!'
            }
                .styles
        )
    }
}

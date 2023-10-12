package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.type.Mention
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.ColonMentionDelimiter
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.url
import com.jeanbarrossilva.orca.std.styledstring.type.test.mention.username
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StringExtensionsTests {
  @Test
  fun `GIVEN a string with a mention WHEN converting it into a styled string THEN the mention is preserved`() {
    assertEquals(
      buildStyledString {
        +"Hello, "
        mention(Mention.url) { +Mention.username }
        +('!')
      },
      "Hello, @${Mention.username}!".toStyledString { Mention.url }
    )
  }

  @Test
  fun `GIVEN a string with a mention containing two delimiters WHEN converting it into a styled string THEN the leading delimiter is ignored`() {
    assertEquals(
      buildStyledString {
        +"Hello, ${Mention.SYMBOL}"
        mention(Mention.url) { +Mention.username }
        +'!'
      },
      ("Hello, " + Mention.SYMBOL + Mention.SYMBOL + Mention.username + '!').toStyledString {
        Mention.url
      }
    )
  }

  @Test
  fun `GIVEN a string with multiple mentions WHEN converting it into a styled string THEN the mentions are preserved`() {
    assertEquals(
      buildStyledString {
        +"Hello, "
        mention(Mention.url) { +Mention.username }
        +" and "
        mention(URL("https://mastodon.social/@christianselig")) { +"christianselig" }
        +'!'
      },
      "Hello, @${Mention.username} and @christianselig!"
        .toStyledString {
          when (it) {
            7 -> Mention.url
            28 -> URL("https://mastodon.social/@christianselig")
            else -> throw IllegalStateException("🫠")
          }
        }
    )
  }

  @Test
  fun `GIVEN a string with a different mention delimiter WHEN converting it into a styled string THEN the mentions start with their default symbol`() {
    assertEquals(
      "Hello, " + Mention.SYMBOL + Mention.username + '!',
      "Hello, :${Mention.username}!"
        .toStyledString(mentionDelimiter = ColonMentionDelimiter)
        .toString()
    )
  }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ContentTests {
  @Ignore("Depends on the implementation of instance-specific URL resources.")
  @Test
  fun `GIVEN a text with a URL without providing a headline WHEN creating content from it THEN it throws`() {
    assertFailsWith<Content.UnprovidedHeadlineException> {
      Content.from(buildStyledString { +Highlight.sample.url.toString() }) { null }
    }
  }

  @Test
  fun `GIVEN a text with a single trailing URL WHEN creating content from it THEN it's removed`() {
    assertEquals(
      StyledString("🥸"),
      Content.from(buildStyledString { +"🥸 ${Highlight.sample.url}" }) { Headline.sample }.text
    )
  }

  @Test
  fun `GIVEN a text with a trailing link WHEN creating content from it THEN `() {
    assertEquals(
      buildStyledString {
        +"😗 "
        link(Highlight.sample.url) { +"🔗" }
      },
      Content.from(
          buildStyledString {
            +"😗 "
            link(Highlight.sample.url) { +"🔗" }
          }
        ) {
          Headline.sample
        }
        .text
    )
  }

  @Test
  fun `GIVEN a text with two trailing URLs WHEN creating content from it THEN they're kept`() {
    assertEquals(
      buildStyledString { +"🫨 ${Highlight.sample.url} ${Highlight.sample.url}" },
      Content.from(buildStyledString { +"🫨 ${Highlight.sample.url} ${Highlight.sample.url}" }) {
          Headline.sample
        }
        .text
    )
  }
}

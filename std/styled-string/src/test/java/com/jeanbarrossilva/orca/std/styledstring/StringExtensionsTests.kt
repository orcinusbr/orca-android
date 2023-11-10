package com.jeanbarrossilva.orca.std.styledstring

import assertk.assertThat
import assertk.assertions.containsExactly
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import kotlin.test.Test

internal class StringExtensionsTests {
  @Test
  fun transformsEachRegexMatchingPortion() {
    assertThat(
        "me@jeanbarrossilva.com, john@appleseed.com".map(Email.regex) { indices, _ -> indices }
      )
      .containsExactly(0..21, 24..41)
  }
}

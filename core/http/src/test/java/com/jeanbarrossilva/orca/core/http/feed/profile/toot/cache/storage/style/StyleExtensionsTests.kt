package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style

import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style.name
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import kotlin.test.Test

internal class StyleExtensionsTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingNameOfNonexistentStyle() {
    object : Style {
        override val indices = IntRange.EMPTY

        override fun at(indices: IntRange): Style {
          throw UnsupportedOperationException()
        }
      }
      .name
  }
}

/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage.style

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.name
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

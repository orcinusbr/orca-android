/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page

import assertk.assertFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.messageContains
import kotlin.test.Test

internal class PagesTests {
  @Test
  fun negativePageIsInvalid() {
    assertFailure { Pages.validate(-2) }.isInstanceOf<Pages.NegativeException>()
  }

  @Test
  fun noneMarkerIsInvalid() {
    assertFailure { Pages.validate(Pages.NONE) }.isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun throwsMentioningNoneMarkerWhenValidatingIt() =
    assertFailure { Pages.validate(Pages.NONE) }.messageContains(Pages::NONE.name)

  @Test fun pageZeroIsValid() = Pages.validate(0)

  @Test fun positivePageIsValid() = Pages.validate(1)
}

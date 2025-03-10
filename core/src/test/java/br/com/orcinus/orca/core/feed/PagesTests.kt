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

package br.com.orcinus.orca.core.feed

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isZero
import assertk.assertions.messageContains
import br.com.orcinus.orca.std.func.test.monad.isFailed
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import kotlin.test.Test

internal class PagesTests {
  @Test
  fun negativePageIsInvalid() {
    assertThat(Pages.validate(-2)).isFailed().isInstanceOf<Pages.NegativeException>()
  }

  @Test
  fun noneMarkerIsInvalid() {
    assertThat(Pages.validate(Pages.NONE)).isFailed().isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun throwsMentioningNoneMarkerWhenValidatingIt() =
    assertThat(Pages.validate(Pages.NONE)).isFailed().messageContains(Pages::NONE.name)

  @Test fun pageZeroIsValid() = assertThat(Pages.validate(0)).isSuccessful().isZero()

  @Test fun positivePageIsValid() = assertThat(Pages.validate(1)).isSuccessful().isEqualTo(1)
}

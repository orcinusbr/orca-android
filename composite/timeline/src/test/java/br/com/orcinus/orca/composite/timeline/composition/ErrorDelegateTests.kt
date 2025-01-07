/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import br.com.orcinus.orca.platform.testing.context
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ErrorDelegateTests {
  @Test
  fun errorIsInitiallyNull() {
    assertThat(ErrorDelegate(CompositionTextField(context)).error).isNull()
  }

  @Test
  fun errorIsNotNullWhenShown() {
    assertThat(ErrorDelegate(CompositionTextField(context)).apply { toggle(":P") }.error)
      .isEqualTo(":P")
  }

  @Test
  fun errorIsNullWhenItIsHidden() {
    assertThat(
        ErrorDelegate(CompositionTextField(context))
          .apply {
            toggle(":P")
            toggle(null)
          }
          .error
      )
      .isNull()
  }

  @Test
  fun showsErrorOnceWhenRequestingMultipleTimes() {
    spyk(ErrorDelegate(CompositionTextField(context))) {
      repeat(2) { toggle(":P") }
      verify(exactly = 1) { showWhenLaidOut() }
    }
  }

  @Test
  fun doesNotHideInitiallyHiddenErrorUponRequest() {
    spyk(ErrorDelegate(CompositionTextField(context))) {
      toggle(null)
      verify(exactly = 0) { hideWhenLaidOut() }
    }
  }

  @Test
  fun hidesErrorOnceWhenRequestingMultipleTimes() {
    spyk(ErrorDelegate(CompositionTextField(context))) {
      toggle(":P")
      repeat(2) { toggle(null) }
      verify(exactly = 1) { hideWhenLaidOut() }
    }
  }
}

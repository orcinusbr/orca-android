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

package com.jeanbarrossilva.orca.ext.testing

import assertk.assertThat
import com.jeanbarrossilva.testing.hasPropertiesEqualToThoseOf
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test
  fun passesWhenAssertingThatAnObjectHasPropertiesEqualToThoseOfAnotherOneWhenItDoes() {
    assertThat(
        object {
          @Suppress("unused") val msg = "🇮🇹"
        }
      )
      .hasPropertiesEqualToThoseOf(
        object {
          @Suppress("unused") val msg = "🇮🇹"
        }
      )
  }

  @Test
  fun passesWhenAssertingThatAnObjectHasPropertiesEqualToThoseOfAnotherOneWhenTheFormerDoesNotHaveProperties() {
    assertThat(object {})
      .hasPropertiesEqualToThoseOf(
        object {
          @Suppress("unused") val msg = "🇮🇹"
        }
      )
  }

  @Test
  fun passesWhenAssertingThatAnObjectHasPropertiesEqualToThoseOfAnotherOneWhenTheLatterDoesNotHaveProperties() {
    assertThat(object {})
      .hasPropertiesEqualToThoseOf(
        object {
          @Suppress("unused") val msg = "🇮🇹"
        }
      )
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatAnObjectHasPropertiesEqualToThoseOfAnotherOneWhenItDoesNot() {
    assertThat(
        object {
          @Suppress("unused") val msg = "🇮🇹"
        }
      )
      .hasPropertiesEqualToThoseOf(
        object {
          @Suppress("unused") val msg = "🇳🇴"
        }
      )
  }
}

/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.ext.testing

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isZero
import assertk.assertions.prop
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test fun createsAssertWithKClass() = assertThat<Any>().isSameInstanceAs(Any::class)

  @Test fun gets() = assertThat(assertThat(0)).prop(Assert<Int>::get).isZero()

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

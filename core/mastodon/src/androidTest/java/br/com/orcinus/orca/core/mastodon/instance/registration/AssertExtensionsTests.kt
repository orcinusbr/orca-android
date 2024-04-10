/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance.registration

import assertk.assertThat
import kotlin.math.PI
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test
  fun passesWhenAssertingThatIterableContainsAnyOfTheSpecifiedElementsAndItHasSomeOfThem() {
    assertThat(listOf(0, ":P")).containsAny(":P", PI)
  }

  @Test
  fun passesWhenAssertingThatIterableContainsAnyOfTheSpecifiedElementsAndItHasAllOfThem() {
    assertThat(listOf(2, ":)")).containsAny(2, ":)")
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatIterableContainsAnyOfTheSpecifiedElementsAndItDoesNot() {
    assertThat(listOf(0, 2)).containsAny(4, 16)
  }
}
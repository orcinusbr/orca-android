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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request

import assertk.assertThat
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenAssertingThatAnElementIsRepeatedInAnArrayANegativeAmountOfTimes() {
    assertThat(arrayOf(0, 1)).hasRepetitionCountOf(-1)
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatAnElementIsRepeatedInAnEmptyArrayAnAmountOfTimesGreaterThanZero() {
    assertThat(emptyArray<Int>()).hasRepetitionCountOf(1)
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatAnElementIsRepeatedInAnArrayAndItIsNot() {
    assertThat(arrayOf(0, 1)).hasRepetitionCountOf(2)
  }

  @Test
  fun passesWhenAssertingThatAnElementIsRepeatedInAnArrayAndItIs() {
    assertThat(arrayOf(0, 0)).hasRepetitionCountOf(2)
  }
}

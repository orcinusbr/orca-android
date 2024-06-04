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

import androidx.annotation.IntRange
import assertk.Assert
import assertk.assertions.containsExactly
import assertk.assertions.isNotEmpty
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.opentest4j.AssertionFailedError

/**
 * Asserts that the [Array] contains an element and the amount by which it is repeated is
 * [repetitionCount].
 *
 * @param repetitionCount Amount of repetitions of the same element that the [Array] should contain.
 * @throws AssertionFailedError If the element isn't repeated [repetitionCount] times.
 * @throws IllegalArgumentException If [repetitionCount] is negative.
 */
@Throws(AssertionFailedError::class, IllegalArgumentException::class)
internal inline fun <reified T> Assert<Array<T>>.hasRepetitionCountOf(
  @IntRange(from = 0) repetitionCount: Int
): Assert<Array<T>> {
  require(repetitionCount >= 0) { "Repetition count should be positive." }
  if (repetitionCount > 0) {
    isNotEmpty()
  }
  given { actual ->
    val repeatedElement = actual.first()
    containsExactly(*Array(repetitionCount) { repeatedElement })
    val actualRepetitionCount =
      actual.filter { actualElement -> actualElement == repeatedElement }.size
    val quantify = { times: Int -> times.toString() + if (times == 1) "time" else "times" }
    if (repetitionCount != actualRepetitionCount) {
      expected(
        "$repeatedElement to be repeated in $actual ${show(quantify(repetitionCount))} but it " +
          "was repeated ${show(quantify(actualRepetitionCount))} instead."
      )
    }
  }
  return this
}

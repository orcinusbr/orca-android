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

package br.com.orcinus.orca.ext.functional

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isSuccess
import assertk.assertions.prop
import kotlin.test.Test

internal class MonadsTests {
  private val exception = Exception()

  @Test
  fun failsWhenJoiningEachElementOfAFailedIterableIntoASingleResult() =
    assertThat(Result)
      .transform("failure") { it.failure<List<Int>>(exception) }
      .transform("onEach") { result -> result.onEach { n -> Result.success(n) } }
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenJoiningEachElementOfAResultingIterableIntoASingleResultAndOneOfThemIsAFailure() =
    assertThat(Result)
      .transform("success") { it.success(listOf(1, 2, 3)) }
      .transform("onEach") { result ->
        result.onEach { n -> if (n % 2 != 0) Result.success(n) else Result.failure(exception) }
      }
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun joinsEachElementOfAResultingIterableIntoASingleResult() =
    assertThat(Result)
      .transform("success") { it.success(listOf(1, 2, 3)) }
      .transform("onEach") { result -> result.onEach { n -> Result.success(n * 2) } }
      .isSuccess()
      .containsExactly(2, 4, 6)

  @Test
  fun failsWhenFlatMappingAFailedValue() =
    assertThat(Result)
      .transform("failure") { it.failure<Result<Int>>(exception) }
      .transform("flatMap") { it.flatMap { Result.success(0) } }
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenFlatteningAFailedValue() =
    assertThat(Result)
      .transform("failure") { it.failure<Result<Any>>(exception) }
      .prop(Result<Result<Any>>::flatten)
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun obtainsTheInnerFailureResultWhenGettingTheValueOfTheFlattenedReceiverOne() =
    assertThat(Result)
      .transform("success") { it.success(Result.failure<Any>(exception)) }
      .prop(Result<Result<Any>>::flatten)
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun obtainsTheValueOfInnerResultWhenGettingThatOfTheFlattenedReceiverOne() =
    assertThat(Result)
      .transform("success") { it.success(Result.success(0)) }
      .prop(Result<Result<Int>>::flatten)
      .isSuccess()
      .isEqualTo(0)

  @Test
  fun failsWhenConvertingAFailedValueIntoAResultOfUnit() =
    assertThat(Result)
      .transform("failure") { it.failure<Any>(exception) }
      .prop(Result<Any>::unit)
      .isFailure()
      .isSameInstanceAs(exception)

  @Test
  fun convertsASuccessfulValueIntoAResultOfUnit() =
    assertThat(Result)
      .transform("success") { it.success(0) }
      .prop(Result<Int>::unit)
      .isSuccess()
      .isSameInstanceAs(Unit)
}

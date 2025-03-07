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

package br.com.orcinus.orca.std.func.monad

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.std.func.test.monad.failed
import br.com.orcinus.orca.std.func.test.monad.isFailed
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import br.com.orcinus.orca.std.func.test.monad.successful
import kotlin.test.Test

internal class MaybeTests {
  private val exception = Exception()

  @Test
  fun isSuccessfulIsFalseWhenMaybeIsFailed() =
    assertThat(Maybe).failed<_, Any>(exception).prop(Maybe<Exception, Any>::isSuccessful).isFalse()

  @Test
  fun isSuccessfulIsTrueWhenMaybeIsSuccessful() =
    assertThat(Maybe).successful<Exception, _>(0).prop(Maybe<Exception, Int>::isSuccessful).isTrue()

  @Test
  fun isFailedIsTrueWhenMaybeIsFailed() =
    assertThat(Maybe).failed<_, Any>(exception).prop(Maybe<Exception, Any>::isFailed).isTrue()

  @Test
  fun isFailedIsFalseWhenMaybeIsSuccessful() =
    assertThat(Maybe).successful<Exception, _>(0).prop(Maybe<Exception, Int>::isFailed).isFalse()

  @Test
  fun failsWhenJoiningEachElementOfAFailedIterableIntoASingleResult() =
    assertThat(Maybe)
      .failed<_, List<Int>>(exception)
      .transform("onEach") { maybe -> maybe.onEach { n -> Maybe.successful(n) } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenJoiningEachElementOfAResultingIterableIntoASingleResultAndOneOfThemIsAFailure() =
    assertThat(Maybe)
      .successful<Exception, _>(listOf(1, 2, 3))
      .transform("onEach") { result ->
        result.onEach { n -> if (n % 2 != 0) Maybe.successful(n) else Maybe.failed(exception) }
      }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun joinsEachElementOfAResultingIterableIntoASingleResult() =
    assertThat(Maybe)
      .successful<Exception, _>(listOf(1, 2, 3))
      .transform("onEach") { maybe -> maybe.onEach { n -> Maybe.successful(n * 2) } }
      .isSuccessful()
      .containsExactly(2, 4, 6)

  @Test
  fun failsWhenFlatMappingAFailedValue() =
    assertThat(Maybe)
      .failed<_, Maybe<Exception, Int>>(exception)
      .transform("flatMap") { it.flatMap { Maybe.successful(0) } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenFlatteningAFailedValue() =
    assertThat(Maybe)
      .failed<_, Maybe<Exception, Any>>(exception)
      .prop(Maybe<Exception, Maybe<Exception, Any>>::flatten)
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun obtainsTheInnerFailureResultWhenGettingTheValueOfTheFlattenedReceiverOne() =
    assertThat(Maybe)
      .successful<Exception, _>(Maybe.failed<_, Any>(exception))
      .prop(Maybe<Exception, Maybe<Exception, Any>>::flatten)
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun obtainsTheValueOfInnerResultWhenGettingThatOfTheFlattenedReceiverOne() =
    assertThat(Maybe)
      .successful<Exception, _>(Maybe.successful<Exception, _>(0))
      .prop(Maybe<Exception, Maybe<Exception, Int>>::flatten)
      .isSuccessful()
      .isEqualTo(0)

  @Test
  fun failsWhenConvertingAFailedValueIntoAResultOfUnit() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .prop(Maybe<Exception, Any>::unit)
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun convertsASuccessfulValueIntoAResultOfUnit() =
    assertThat(Maybe)
      .successful<Exception, _>(0)
      .prop(Maybe<Exception, Int>::unit)
      .isSuccessful()
      .isSameInstanceAs(Unit)
}

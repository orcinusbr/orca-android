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

import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.cause
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.isZero
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
  fun onSuccessfulReturnsMaybeWhenItIsSuccessful() =
    assertThat(Maybe).successful<Exception, _>(0).all {
      given { transform("onSuccessful") { maybe -> maybe.onSuccessful {} }.isEqualTo(it) }
    }

  @Test
  fun onSuccessfulReturnsMaybeWhenItIsFailed() =
    assertThat(Maybe).failed<_, Any>(exception).all {
      given { transform("onSuccessful") { maybe -> maybe.onSuccessful {} }.isEqualTo(it) }
    }

  @Test
  fun doesNotCallSuccessfulCallbackWhenMaybeIsFailed() {
    var didCallCallback = false
    Maybe.failed<_, Any>(exception).onSuccessful { didCallCallback = true }
    assertThat(didCallCallback, name = "didCallCallback").isFalse()
  }

  @Test
  fun callsSuccessfulCallbackWhenMaybeIsSuccessful() {
    var didCallCallback = false
    Maybe.successful<Exception, _>(0).onSuccessful { didCallCallback = true }
    assertThat(didCallCallback, name = "didCallCallback").isTrue()
  }

  @Test
  fun failsWhenJoiningEachElementOfAFailedIterableIntoASingleMaybe() =
    assertThat(Maybe)
      .failed<_, List<Int>>(exception)
      .transform("onEach") { maybe -> maybe.onEach { n -> Maybe.successful(n) } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenJoiningEachElementOfAResultingIterableIntoASingleMaybeAndTheTransformationThrowsAnUnexpectedException() {
    assertFailure {
        Maybe.successful<UnsupportedOperationException, _>(listOf(1, 2, 3)).join<_, _, Int> {
          error("👵🏽")
        }
      }
      .isInstanceOf<UnexpectedFailureException>()
  }

  @Test
  fun failsWhenJoiningEachElementOfAResultingIterableIntoASingleMaybeAndOneOfThemIsAFailure() =
    assertThat(Maybe)
      .successful<Exception, _>(listOf(1, 2, 3))
      .transform("onEach") { maybe ->
        maybe.onEach { n -> if (n % 2 != 0) Maybe.successful(n) else Maybe.failed(exception) }
      }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun joinsEachElementOfAResultingIterableIntoASingleMaybe() =
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
  fun obtainsTheInnerFailedMaybeWhenGettingTheValueOfTheFlattenedReceiverOne() =
    assertThat(Maybe)
      .successful<Exception, _>(Maybe.failed<_, Any>(exception))
      .prop(Maybe<Exception, Maybe<Exception, Any>>::flatten)
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun obtainsTheValueOfNestedMaybeWhenGettingThatOfTheFlattenedReceiverOne() =
    assertThat(Maybe)
      .successful<Exception, _>(Maybe.successful<Exception, _>(0))
      .prop(Maybe<Exception, Maybe<Exception, Int>>::flatten)
      .isSuccessful()
      .isEqualTo(0)

  @Test
  fun failsWhenConvertingAFailedValueIntoAUnitMaybe() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .prop(Maybe<Exception, Any>::unit)
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun convertsASuccessfulValueIntoAUnitMaybe() =
    assertThat(Maybe)
      .successful<Exception, _>(0)
      .prop(Maybe<Exception, Int>::unit)
      .isSuccessful()
      .isSameInstanceAs(Unit)

  @Test
  fun failsWhenCombiningAFailedValueAndASuccessfulOne() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .transform("combine") { it.combine(Maybe.successful(0)) { _, _ -> "👩🏻‍💻⛓️‍💥" } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun failsWhenCombiningTwoFailedValues() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .transform("combine") { it.combine(Maybe.failed<_, Any>(Exception())) { _, _ -> "👤🧱" } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun combinesTwoSuccessfulValues() =
    assertThat(Maybe)
      .successful<Exception, _>(2)
      .transform("combine") { it.combine(Maybe.successful(2), Int::times) }
      .isSuccessful()
      .isEqualTo(4)

  @Test
  fun failsWhenMappingAFailedValueToAnotherOne() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .transform("map") { it.map { 0 } }
      .isFailed()
      .isSameInstanceAs(exception)

  @Test
  fun mapsASuccessfulValueToAnotherOne() =
    assertThat(Maybe)
      .successful<Exception, _>(2)
      .transform("map") { it.map(2::times) }
      .isSuccessful()
      .isEqualTo(4)

  @Test
  fun failsWithTheTransformedExceptionWhenCallingFailWithOnAFailedValue() =
    assertThat(Maybe)
      .failed<_, Any>(exception)
      .transform("failWith") { it.failWith(::RuntimeException) }
      .isFailed()
      .cause()
      .isSameInstanceAs(exception)

  @Test
  fun returnsTheSuccessfulValueWhenCallingFailWithOnIt() =
    assertThat(Maybe)
      .successful<Exception, _>(0)
      .transform("failWith") { it.failWith(::RuntimeException) }
      .isSuccessful()
      .isZero()
}

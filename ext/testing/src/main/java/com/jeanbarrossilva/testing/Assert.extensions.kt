/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.testing

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import org.opentest4j.AssertionFailedError

/**
 * Asserts that the value's properties have the same name and hold data equal to [other]'s. It will
 * not fail if it has ones that aren't present in [other], vice-versa or none at all, only if the
 * ones that are named equally have been assigned differently.
 *
 * @param F Value on which the assertion will be performed.
 * @param S Value to be compared to [F].
 * @param other Object whose properties will be compared to the value's.
 * @throws AssertionFailedError If a property of the value has a counterpart in [other] but has
 *   different data assigned to it.
 */
@Throws(AssertionFailedError::class)
inline fun <F : Any, reified S : Any> Assert<F>.hasPropertiesEqualToThoseOf(other: S): Assert<F> {
  given { value ->
    value::class
      .declaredMemberProperties
      .associateWithEquallyNamedDeclaredProperties<_, S>()
      .mapKeys { (expectedProperty, _) -> expectedProperty.get(value) }
      .forEach { (expected, actualProperty) ->
        expected(
          expected,
          actual = actualProperty.get(other),
          actualProperty.name,
          actualParentClass = other::class
        )
      }
  }
  return this
}

/**
 * Fails the assertion if [expected] isn't equal to [actual], exposing [actual]'s [KProperty] name
 * and its parent [KClass] in the error message for a more detailed description of what was
 * mismatched.
 *
 * @param expected Value that [actual] is expected to equal to.
 * @param actual Data held by the [KProperty] that should equal to the [expected] one.
 * @param actualName Name of [actual]'s [KProperty].
 * @param actualParentClass Name of the [KClass] in which [actual]'s [KProperty] is declared.
 * @throws AssertionFailedError If [expected] isn't equal to [actual].
 */
@PublishedApi
internal fun Assert<*>.expected(
  expected: Any?,
  actual: Any?,
  actualName: String,
  actualParentClass: KClass<*>
) {
  if (expected != actual) {
    expected(
      "${actualParentClass.simpleName}.$actualName to be:${show(expected)} but was:${show(actual)}"
    )
  }
}

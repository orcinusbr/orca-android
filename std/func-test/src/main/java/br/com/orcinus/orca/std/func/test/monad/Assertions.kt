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

@file:JvmName("Assertions")

package br.com.orcinus.orca.std.func.test.monad

import assertk.Assert
import assertk.assertions.isNotNull
import assertk.assertions.prop
import assertk.assertions.support.expected
import assertk.assertions.support.show
import br.com.orcinus.orca.std.func.monad.Maybe
import org.opentest4j.AssertionFailedError

/**
 * Asserts that the [Maybe] holds a successful value.
 *
 * @param V The successful value.
 * @throws AssertionFailedError If the [Maybe] holds a failure.
 */
@Throws(AssertionFailedError::class)
fun <V> Assert<Maybe<*, V>>.isSuccessful() =
  try {
    prop(Maybe<*, V>::getValueOrThrow)
  } catch (exception: Exception) {
    expected("to be successful but was failed:${show(exception)}")
  }

/**
 * Asserts that the [Maybe] holds a failure.
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @throws AssertionFailedError If the [Maybe] holds a successful value.
 */
@Throws(AssertionFailedError::class)
fun <E : Exception> Assert<Maybe<E, *>>.isFailed() =
  transform {
      it.takeIf(Maybe<E, *>::isFailed)
        ?: expected("to be failed but was successful:${show(it.getValueOrThrow())}")
    }
    .prop(Maybe<E, *>::getExceptionOrNull)
    .transform {
      @Suppress("UNCHECKED_CAST")
      it as E?
    }
    .isNotNull()

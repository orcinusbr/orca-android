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

@file:JvmName("Factories")

package br.com.orcinus.orca.std.func.test.monad

import assertk.Assert
import br.com.orcinus.orca.std.func.monad.Maybe

/**
 * Produces an assertable, successful [Maybe].
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V The successful value.
 * @param value Successful value to be held.
 */
fun <E : Exception, V> Assert<Maybe.Companion>.successful(value: V) =
  transform("successful") { it.successful<E, _>(value) }

/**
 * Produces an assertable, failed [Maybe].
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V The successful value.
 * @param exception [Exception] because of which the value cannot be obtained.
 */
fun <E : Exception, V> Assert<Maybe.Companion>.failed(exception: E) =
  transform("failed") { it.failed<_, V>(exception) }

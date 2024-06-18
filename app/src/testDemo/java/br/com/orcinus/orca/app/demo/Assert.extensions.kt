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

package br.com.orcinus.orca.app.demo

import assertk.Assert
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import kotlin.reflect.KClass

/**
 * Asserts that the given elements' [KClass]es equate to the given ones.
 *
 * @param E Element in the [Iterable].
 * @param I [Iterable] in which the elements are.
 * @param classes [KClass]es of which each element is expected to be an instance, in the specified
 *   order.
 */
internal fun <E : Any, I : Iterable<E>> Assert<I>.containsExactlyInstancesOf(
  vararg classes: KClass<out E>
): Assert<I> {
  extracting { it::class }.containsExactly(*classes)
  return this
}

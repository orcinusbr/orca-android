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

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts that the [Iterable] contains at least one of the specified [elements].
 *
 * @param E Element in the [Iterable].
 * @param I [Iterable] by which any of the [elements] are expected to be contained.
 */
internal fun <E, I : Iterable<E>> Assert<I>.containsAny(vararg elements: E): Assert<I> {
  given { actual ->
    val elementsAsList = elements.toList()
    val containsNone = elementsAsList.none { element -> element in actual }
    if (containsNone) {
      expected("to contain any of:${show(elements)} but was:${show(actual)}")
    }
  }
  return this
}

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

package br.com.orcinus.orca.std.injector.module.replacement

internal class EmptyReplacer<E, S>(
  override val caching: Caching<E, S>,
  override val selector: (E) -> S
) : Replacer<E, S, Unit>() {
  override val size = 0

  override fun iterator(): Iterator<E> {
    return object : Iterator<E> {
      override fun hasNext(): Boolean {
        return false
      }

      override fun next(): E {
        throw NoSuchElementException("Empty replacer has no elements!")
      }
    }
  }

  override fun isEmpty(): Boolean {
    return true
  }

  override fun append(placement: Any?, index: Int, element: E) {}

  override fun onPreparationForReplacement(index: Int) {}
}

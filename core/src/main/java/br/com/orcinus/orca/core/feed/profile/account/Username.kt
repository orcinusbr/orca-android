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

package br.com.orcinus.orca.core.feed.profile.account

import br.com.orcinus.orca.core.instance.domain.Domain

/** Unique identifier of an [Account]. */
@JvmInline
value class Username
@Throws(BlankValueException::class, IllegalValueException::class)
internal constructor(internal val value: String) {
  /** [IllegalArgumentException] thrown if the [value] is blank. */
  class BlankValueException internal constructor() :
    IllegalArgumentException("Username cannot be empty.")

  /**
   * [IllegalArgumentException] thrown if the [value] contains any illegal characters.
   *
   * @param illegalCharacters [Char]s that make the [value] illegal.
   */
  class IllegalValueException internal constructor(illegalCharacters: List<Char>) :
    IllegalArgumentException("Username cannot contain: $illegalCharacters.")

  init {
    ensureNonBlankness()
    ensureLegality()
  }

  override fun toString(): String {
    return "@$value"
  }

  /**
   * Ensures that the [value] isn't blank.
   *
   * @throws BlankValueException If the [value] is blank.
   */
  @Throws(BlankValueException::class)
  private fun ensureNonBlankness() {
    if (value.isBlank()) {
      throw BlankValueException()
    }
  }

  /**
   * Ensures that the [value] is legal.
   *
   * @throws IllegalValueException If the [value] is illegal.
   */
  @Throws(IllegalValueException::class)
  private fun ensureLegality() {
    Domain.doOnIllegality(value) { throw IllegalValueException(it) }
  }
}

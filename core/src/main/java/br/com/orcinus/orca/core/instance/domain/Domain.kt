/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.instance.domain

import br.com.orcinus.orca.ext.uri.URIBuilder
import java.io.Serializable
import java.net.URI

/**
 * An instance's unique identifier.
 *
 * @param value [String] that represents this [Domain].
 */
@JvmInline
value class Domain(private val value: String) : Serializable {
  /** [URI] that leads to this [Domain]. */
  val uri
    get() = URIBuilder.url().scheme("https").host(value).build()

  /** [IllegalArgumentException] thrown if the [value] is blank. */
  class BlankValueException internal constructor() :
    IllegalArgumentException("Domain cannot be empty.")

  /**
   * [IllegalArgumentException] thrown if the [value] contains any of the [illegalCharacters].
   *
   * @param illegalCharacters [Char]s that make the [value] illegal.
   */
  class IllegalValueException internal constructor(illegalCharacters: List<Char>) :
    IllegalArgumentException("Value cannot contain: $illegalCharacters.")

  /**
   * [IllegalArgumentException] thrown if the [value] doesn't have a top-level domain.
   *
   * @param value Domain that lacks a top-level domain.
   */
  class ValueWithoutTopLevelDomainException internal constructor(value: String) :
    IllegalArgumentException("\"$value\" doesn't have a top-level domain.")

  init {
    ensureNonBlankness()
    ensureLegality()
    ensureTopLevelDomainPresence()
  }

  override fun toString(): String {
    return value
  }

  /**
   * Ensures that the [value] is not blank.
   *
   * @throws BlankValueException If the [value] is blank.
   */
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
  private fun ensureLegality() {
    doOnIllegality(value) { throw IllegalValueException(it) }
  }

  /**
   * Ensures that the [value] has a top-level domain.
   *
   * @throws ValueWithoutTopLevelDomainException If the [value] doesn't have a top-level domain.
   */
  private fun ensureTopLevelDomainPresence() {
    if (!containsTopLevelDomain(value)) {
      throw ValueWithoutTopLevelDomainException(value)
    }
  }

  companion object {
    /** [Char]s that the [value] shouldn't contain. */
    internal val illegalCharacters = arrayOf(' ', '@')

    /**
     * Verifies whether the [value] is valid.
     *
     * @param value [String] whose validity will be verified.
     */
    fun isValid(value: String): Boolean {
      return value.isNotBlank() && value.isLegal && containsTopLevelDomain(value)
    }

    /**
     * Runs the [action] if the [value] contains any of the [illegalCharacters].
     *
     * @param value [String] to check against.
     * @param action Callback called if the [value] is considered to be illegal.
     */
    internal fun doOnIllegality(value: String, action: (illegal: List<Char>) -> Unit) {
      val illegal = illegalCharacters.filter { it in value }
      val isIllegal = illegal.any { it in value }
      if (isIllegal) {
        action(illegal)
      }
    }

    /**
     * Whether the [value] contains a top-level domain.
     *
     * @param value [String] whose top-level domain's presence will be checked.
     */
    private fun containsTopLevelDomain(value: String): Boolean {
      return value.split('.').getOrNull(1)?.isNotBlank() ?: false
    }
  }
}

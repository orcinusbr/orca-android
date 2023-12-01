/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.instance.domain

import java.net.URL

/**
 * An instance's unique identifier.
 *
 * @param value [String] that represents this [Domain].
 */
@JvmInline
value class Domain(private val value: String) {
  /** [URL] that leads to this [Domain]. */
  val url
    get() = URL("https", value, "")

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

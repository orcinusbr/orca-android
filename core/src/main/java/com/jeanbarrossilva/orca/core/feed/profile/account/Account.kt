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

package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.feed.profile.account.Account.BlankUsernameException
import com.jeanbarrossilva.orca.core.feed.profile.account.Account.Companion.of
import com.jeanbarrossilva.orca.core.feed.profile.account.Account.IllegalUsernameException
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.instance.domain.isLegal
import java.io.Serializable

/**
 * Identifies the user within the [domain].
 *
 * An [Account] can be instantiated either through the [at] extension function or by parsing a given
 * [String] with [of].
 *
 * @param username Unique name that can be modified.
 * @param domain [Domain] of the server in which this [Account] is.
 * @throws BlankUsernameException If the [username] is blank.
 * @throws IllegalUsernameException If the [username] contains any illegal characters.
 */
data class Account internal constructor(val username: String, val domain: Domain) : Serializable {
  /** [IllegalArgumentException] thrown if the [username] is blank. */
  class BlankUsernameException internal constructor() :
    IllegalArgumentException("An account cannot have a blank username.")

  /**
   * [IllegalArgumentException] thrown if the [username] contains any illegal characters.
   *
   * @param illegalCharacters [Char]s that make the [username] illegal.
   */
  class IllegalUsernameException internal constructor(illegalCharacters: List<Char>) :
    IllegalArgumentException("Username cannot contain: $illegalCharacters")

  init {
    ensureUsernameNonBlankness()
    ensureUsernameLegality()
  }

  override fun toString(): String {
    return username + SEPARATOR + domain
  }

  /**
   * Ensures that the [username] is not blank.
   *
   * @throws BlankUsernameException If the [username] is blank.
   */
  private fun ensureUsernameNonBlankness() {
    if (username.isBlank()) {
      throw BlankUsernameException()
    }
  }

  /**
   * Ensures that the [username] is legal.
   *
   * @throws IllegalUsernameException If the [username] is illegal.
   */
  private fun ensureUsernameLegality() {
    Domain.doOnIllegality(username) { throw IllegalUsernameException(it) }
  }

  companion object {
    /** [Char] that separates the [username] from the [domain]. */
    private const val SEPARATOR = '@'

    /**
     * [IllegalArgumentException] thrown if a blank [String] is given when parsing it into an
     * [Account].
     *
     * @see of
     */
    class BlankStringException internal constructor() :
      IllegalArgumentException("Cannot parse a blank String.")

    /**
     * Parses the [string] into an [Account].
     *
     * It should be formatted as `"{username}@{domain}"`, with "{username}" and "{domain}" replaced
     * by the actual data they represent; it can also be given as `"{username}"`, without the
     * domain, if a [fallbackDomain] is specified.
     *
     * Any leading or trailing whitespace in both the [string] and the [fallbackDomain] will be
     * ignored.
     *
     * @param string [String] to be parsed.
     * @param fallbackDomain [Domain] to fallback to if the [string] only has a username.
     * @throws BlankStringException If the [string] is blank.
     * @throws BlankUsernameException If the username is blank.
     * @throws IllegalUsernameException If the username contains any illegal characters.
     * @throws Domain.BlankValueException If the domain is not specified in the [string] and the
     *   [fallbackDomain] is `null`.
     * @throws Domain.IllegalValueException If the domain contains any of the
     *   [Domain.illegalCharacters].
     * @throws Domain.ValueWithoutTopLevelDomainException If the domain doesn't have a top-level
     *   domain.
     */
    fun of(string: String, fallbackDomain: String? = null): Account {
      val formattedString = string.trim().ifEmpty { throw BlankStringException() }
      val parts = formattedString.split(SEPARATOR)
      val username = parts.first()
      val containsDomain = parts.size > 1
      val domainValue = if (containsDomain) parts[1].trim() else fallbackDomain?.trim().orEmpty()
      val domain = Domain(domainValue)
      return Account(username, domain)
    }

    /**
     * Verifies whether the username is valid.
     *
     * @param username Username whose validity will be verified.
     */
    fun isUsernameValid(username: String): Boolean {
      return username.isNotBlank() && username.isLegal
    }
  }
}

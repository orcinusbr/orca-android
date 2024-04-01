/*
 * Copyright © 2023-2024 Orca
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

import br.com.orcinus.orca.core.feed.profile.account.Account.Companion.of
import br.com.orcinus.orca.core.instance.domain.Domain
import java.io.Serializable

/**
 * Identifies the user within the [domain].
 *
 * An [Account] can be instantiated either through the [at] extension function or by parsing a given
 * [String] with [of].
 *
 * @param username Unique name that can be modified.
 * @param domain [Domain] of the server in which this [Account] is.
 */
data class Account internal constructor(val username: Username, val domain: Domain) : Serializable {
  override fun toString(): String {
    return username.value + SEPARATOR + domain
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
     * @throws Username.BlankValueException If the username is blank.
     * @throws Username.IllegalValueException If the username contains any illegal characters.
     * @throws Domain.BlankValueException If the domain is not specified in the [string] and the
     *   [fallbackDomain] is `null`.
     * @throws Domain.IllegalValueException If the domain contains any of the
     *   [Domain.illegalCharacters].
     * @throws Domain.ValueWithoutTopLevelDomainException If the domain doesn't have a top-level
     *   domain.
     */
    @Throws(
      BlankStringException::class,
      Username.BlankValueException::class,
      Username.IllegalValueException::class,
      Domain.BlankValueException::class,
      Domain.BlankValueException::class
    )
    fun of(string: String, fallbackDomain: String? = null): Account {
      val formattedString = string.trim().ifEmpty { throw BlankStringException() }
      val parts = formattedString.split(SEPARATOR)
      val username = Username(parts[0])
      val containsDomain = parts.size > 1
      val domainValue = if (containsDomain) parts[1].trim() else fallbackDomain?.trim().orEmpty()
      val domain = Domain(domainValue)
      return Account(username, domain)
    }
  }
}

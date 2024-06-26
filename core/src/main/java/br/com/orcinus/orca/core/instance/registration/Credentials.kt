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

package br.com.orcinus.orca.core.instance.registration

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.std.markdown.email

/**
 * Information with which an [Account] can be registered or logged into.
 *
 * @param email E-mail address. When registering an [Account], ideally would be one that hasn't yet
 *   been used by anyone else in the [Instance], and an existing one when logging in.
 * @param password Private key that provides access to the [Account] alongside the [email].
 * @throws BlankPasswordException If the [password] is blank.
 * @throws InvalidEmailException If the [email] is invalid.
 * @see Registrar.register
 */
data class Credentials
@InternalCoreApi
@Throws(BlankPasswordException::class, InvalidEmailException::class)
constructor(val email: String, val password: String) {
  /**
   * [IllegalArgumentException] thrown if the [email] is not a valid one.
   *
   * @param email E-mail that is invalid.
   */
  class InvalidEmailException internal constructor(email: String) :
    IllegalArgumentException("\"$email\" isn't a valid e-mail.")

  /** [IllegalArgumentException] thrown if the [password] is blank. */
  class BlankPasswordException internal constructor() :
    IllegalArgumentException("Password cannot be blank.")

  init {
    ensureEmailValidity()
    ensurePasswordNonBlankness()
  }

  /**
   * Ensures that the [email] is a valid one, matching it against [Regex.Companion.email].
   *
   * @throws InvalidEmailException If the [email] is invalid.
   */
  @Throws(InvalidEmailException::class)
  private fun ensureEmailValidity() {
    val isEmailInvalid = !Regex.email.matches(email)
    if (isEmailInvalid) {
      throw InvalidEmailException(email)
    }
  }

  /**
   * Ensures that the [password] isn't blank.
   *
   * @throws BlankPasswordException If the [password] is blank.
   */
  @Throws(BlankPasswordException::class)
  private fun ensurePasswordNonBlankness() {
    val isPasswordBlank = password.isBlank()
    if (isPasswordBlank) {
      throw BlankPasswordException()
    }
  }

  companion object
}

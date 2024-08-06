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

package br.com.orcinus.orca.core.test.auth

import br.com.orcinus.orca.core.auth.Authorizer

/**
 * Allows for building an [Authorizer] whose authorization code to be provided and its behavior
 * prior to that provisioning can be modified.
 */
class AuthorizerBuilder {
  /**
   * Callback that is called before the authorization code is provided by the built [Authorizer].
   */
  private var before: (suspend () -> Unit)? = null

  /** Code to be provided for an authorization. */
  private var authorizationCode = DEFAULT_AUTHORIZATION_CODE

  /**
   * Defines the authorization code that is provided upon an authorization request, after [before]
   * is invoked.
   *
   * @param authorizationCode Authorization code to be set.
   */
  fun authorizationCode(authorizationCode: String): AuthorizerBuilder {
    this.authorizationCode = authorizationCode
    return this
  }

  /**
   * Defines the callback to be called prior to the provisioning of the authorization code.
   *
   * @param before Callback to be set.
   */
  fun before(before: suspend () -> Unit): AuthorizerBuilder {
    this.before = before
    return this
  }

  /** Builds the [Authorizer]. */
  fun build(): Authorizer {
    return object : Authorizer() {
      public override suspend fun onAuthorization(): String {
        before?.invoke()
        return authorizationCode
      }
    }
  }

  companion object {
    /** Authorization code that is provided by a built [Authorizer] by default. */
    const val DEFAULT_AUTHORIZATION_CODE = "authorization-code"
  }
}

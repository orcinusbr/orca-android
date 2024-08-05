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

package br.com.orcinus.orca.core.test

import br.com.orcinus.orca.core.auth.Authorizer

/**
 * [Authorizer] that provides a constant authorization code.
 *
 * @param onAuthorization Operation to be executed when authorization is requested to be performed.
 * @see AUTHORIZATION_CODE
 */
class ConstantAuthorizer(private val onAuthorization: () -> Unit = noOpOnAuthorization) :
  Authorizer() {
  override suspend fun authorize(): String {
    onAuthorization()
    return AUTHORIZATION_CODE
  }

  companion object {
    /** Default authorization callback of a [ConstantAuthorizer] which does nothing. */
    private val noOpOnAuthorization = {}

    /** Authorization code provided by [authorize]. */
    const val AUTHORIZATION_CODE = "authorization-code"
  }
}

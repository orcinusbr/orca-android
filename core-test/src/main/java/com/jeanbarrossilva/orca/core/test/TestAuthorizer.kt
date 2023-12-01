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

package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.Authorizer

/**
 * [Authorizer] that provides a fixed authorization code.
 *
 * @param onAuthorize Operation to be performed when [authorize] is called.
 * @see AUTHORIZATION_CODE
 */
class TestAuthorizer(private val onAuthorize: () -> Unit = {}) : Authorizer() {
  override suspend fun authorize(): String {
    onAuthorize()
    return AUTHORIZATION_CODE
  }

  companion object {
    /** Authorization code provided by [authorize]. */
    const val AUTHORIZATION_CODE = "authorization-code"
  }
}

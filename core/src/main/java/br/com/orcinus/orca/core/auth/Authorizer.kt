/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.auth

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor

/** Authorizes the [Actor] through [authorize]. */
abstract class Authorizer @InternalCoreApi constructor() {
  /**
   * Authorizes the [Actor], allowing the application to perform operations on their behalf.
   *
   * @return Resulting authorization code.
   */
  internal suspend fun authorize() = onAuthorization()

  /**
   * Callback called whenever authorization is requested to be performed.
   *
   * @return Resulting authorization code.
   */
  protected abstract suspend fun onAuthorization(): AuthorizationCode

  companion object
}

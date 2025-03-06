/*
 * Copyright © 2025 Orcinus
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

/**
 * Wrapper for the code resulted from having authorized an unauthorized [Actor].
 *
 * @property value The authorization code itself. No validation is ever performed on it; its content
 *   can be anything and of any length (including zero, an empty [String]).
 */
@JvmInline
value class AuthorizationCode @InternalCoreApi constructor(internal val value: String) {
  companion object
}

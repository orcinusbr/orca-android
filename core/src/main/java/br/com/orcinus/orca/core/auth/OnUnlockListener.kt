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

package br.com.orcinus.orca.core.auth

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor

/**
 * Listens to an unlock.
 *
 * @param R Value returned by [onUnlock].
 */
fun interface OnUnlockListener<R> {
  /**
   * Callback run when an unlock is performed.
   *
   * @param actor Authenticated [Actor] resulted from the authentication performed by the unlock to
   *   which this listener listened or a previous one.
   */
  @InternalCoreApi suspend fun onUnlock(actor: Actor.Authenticated): R
}

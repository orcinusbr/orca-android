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

package br.com.orcinus.orca.core.feed.profile

import br.com.orcinus.orca.core.InternalCoreApi
import kotlinx.coroutines.flow.Flow

/** Provides a [Profile] through [onProvision]. */
abstract class ProfileProvider @InternalCoreApi constructor() {
  /**
   * [IllegalArgumentException] thrown when a [Profile] that doesn't exist is requested to be
   * provided.
   */
  class NonexistentProfileException @InternalCoreApi constructor(override val cause: Throwable?) :
    IllegalArgumentException("This profile doesn't exist.")

  /**
   * Gets the [Profile] identified as [id].
   *
   * @param id ID of the [Profile] to be provided.
   * @throws NonexistentProfileException If no [Profile] with such [ID][Profile.id] exists.
   * @see Profile.id
   */
  suspend fun provide(id: String): Flow<Profile> {
    return if (contains(id)) onProvision(id) else throw createNonexistentProfileException()
  }

  /**
   * Whether a [Profile] identified as [id] exists.
   *
   * @param id ID of the [Profile] whose existence will be checked.
   */
  protected abstract suspend fun contains(id: String): Boolean

  /**
   * Gets the [Profile] identified as [id].
   *
   * @param id ID of the [Profile] to be provided.
   * @see Profile.id
   */
  protected abstract suspend fun onProvision(id: String): Flow<Profile>

  /** Creates a variant-specific [NonexistentProfileException]. */
  protected abstract fun createNonexistentProfileException(): NonexistentProfileException
}

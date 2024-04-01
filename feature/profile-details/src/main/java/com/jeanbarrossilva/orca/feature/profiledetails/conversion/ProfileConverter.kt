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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import br.com.orcinus.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails

/** Converts a [Profile] into a [ProfileDetails] through [convert]. */
internal abstract class ProfileConverter {
  /** [ProfileConverter] to fallback to in case this one's [convert] returns `null`. */
  abstract val next: ProfileConverter?

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  fun convert(profile: Profile, colors: Colors): ProfileDetails? {
    return onConvert(profile, colors) ?: next?.convert(profile, colors)
  }

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * Returning `null` signals that this [ProfileConverter] cannot perform the conversion and that
   * the operation should be delegated to the [next] one.
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  protected abstract fun onConvert(profile: Profile, colors: Colors): ProfileDetails?
}

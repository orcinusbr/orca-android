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

package com.jeanbarrossilva.orca.composite.timeline.post.time

import java.time.ZonedDateTime

/** Provides a relative version of a [ZonedDateTime]. */
abstract class RelativeTimeProvider {
  /**
   * Provides the relative version of [dateTime].
   *
   * @param dateTime [ZonedDateTime] whose relative version will be provided.
   */
  internal fun provide(dateTime: ZonedDateTime): String {
    return onProvide(dateTime)
  }

  /**
   * Provides the relative version of [dateTime].
   *
   * @param dateTime [ZonedDateTime] whose relative version will be provided.
   */
  protected abstract fun onProvide(dateTime: ZonedDateTime): String
}

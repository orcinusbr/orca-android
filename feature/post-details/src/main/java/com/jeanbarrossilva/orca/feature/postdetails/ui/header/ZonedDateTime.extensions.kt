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

package com.jeanbarrossilva.orca.feature.postdetails.ui.header

import java.text.DateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Date

internal val ZonedDateTime.formatted: String
  get() = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(toDate())

/** Converts this [ZonedDateTime] into a [Date]. */
private fun ZonedDateTime.toDate(): Date {
  val epochSecond = toEpochSecond()
  val instant = Instant.ofEpochSecond(epochSecond)
  return Date.from(instant)
}

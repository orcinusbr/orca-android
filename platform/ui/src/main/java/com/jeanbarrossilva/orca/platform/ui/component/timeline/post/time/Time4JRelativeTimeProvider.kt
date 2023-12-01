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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import net.time4j.PrettyTime
import net.time4j.base.UnixTime

/** [RelativeTimeProvider] that provides relative time with Time4J. */
internal class Time4JRelativeTimeProvider : RelativeTimeProvider() {
  override fun onProvide(dateTime: ZonedDateTime): String {
    val locale = Locale.getDefault()
    val unixTime = dateTime.toUnixTime()
    val timeZoneID = ZoneId.systemDefault().id
    return PrettyTime.of(locale).printRelative(unixTime, timeZoneID)
  }

  /** Converts this [ZonedDateTime] into a [UnixTime]. */
  private fun ZonedDateTime.toUnixTime(): UnixTime {
    return object : UnixTime {
      override fun getPosixTime(): Long {
        return toEpochSecond()
      }

      override fun getNanosecond(): Int {
        return nano
      }
    }
  }
}

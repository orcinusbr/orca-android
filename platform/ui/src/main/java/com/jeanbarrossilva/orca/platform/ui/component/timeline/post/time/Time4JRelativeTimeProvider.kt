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

import android.content.Context
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import net.time4j.PrettyTime
import net.time4j.android.ApplicationStarter
import net.time4j.base.UnixTime

/**
 * [RelativeTimeProvider] that provides relative time with Time4J.
 *
 * @param context [Context] through which Time4J will be started the first time a relative time is
 *   requested to be provided.
 */
internal class Time4JRelativeTimeProvider(private val context: Context) : RelativeTimeProvider() {
  /** Whether Time4J has been started. */
  private var hasTime4JBeenStarted = false

  override fun onProvide(dateTime: ZonedDateTime): String {
    startTime4JIfUnstarted()
    val locale = Locale.getDefault()
    val unixTime = dateTime.toUnixTime()
    val timeZoneID = ZoneId.systemDefault().id
    return PrettyTime.of(locale).printRelative(unixTime, timeZoneID)
  }

  /** Starts Time4J if it hasn't been started yet. */
  private fun startTime4JIfUnstarted() {
    if (!hasTime4JBeenStarted) {
      ApplicationStarter.initialize(context, true)
      hasTime4JBeenStarted = true
    }
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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import net.time4j.PrettyTime
import net.time4j.base.UnixTime

/** [RelativeTimeProvider] that provides relative time with Time4J. */
internal class Time4JRelativeTimeProvider : RelativeTimeProvider() {
  override fun provide(dateTime: ZonedDateTime): String {
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

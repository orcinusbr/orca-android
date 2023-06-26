package com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import net.time4j.PrettyTime
import net.time4j.base.UnixTime

/** Format in which this [ZonedDateTime] is presented in a relative way. **/
val ZonedDateTime.relative: String
    get() {
        val locale = Locale.getDefault()
        val unixTime = toUnixTime()
        val timeZoneID = ZoneId.systemDefault().id
        return PrettyTime.of(locale).printRelative(unixTime, timeZoneID)
    }

/** Converts this [ZonedDateTime] into a [UnixTime]. **/
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

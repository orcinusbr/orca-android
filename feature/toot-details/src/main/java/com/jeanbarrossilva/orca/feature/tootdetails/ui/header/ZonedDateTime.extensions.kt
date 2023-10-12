package com.jeanbarrossilva.orca.feature.tootdetails.ui.header

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

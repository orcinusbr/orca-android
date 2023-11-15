package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time

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

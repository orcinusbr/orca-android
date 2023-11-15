package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time

import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import java.time.ZonedDateTime

/**
 * [RelativeTimeProvider] that provides the [String] representation of the given [ZonedDateTime].
 */
object TestRelativeTimeProvider : RelativeTimeProvider() {
  public override fun onProvide(dateTime: ZonedDateTime): String {
    return dateTime.toString()
  }
}

package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.test

import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import java.time.ZonedDateTime

/**
 * [RelativeTimeProvider] that provides the [String] representation of the given [ZonedDateTime].
 **/
internal class TestRelativeTimeProvider : RelativeTimeProvider() {
    override fun provide(dateTime: ZonedDateTime): String {
        return dateTime.toString()
    }
}

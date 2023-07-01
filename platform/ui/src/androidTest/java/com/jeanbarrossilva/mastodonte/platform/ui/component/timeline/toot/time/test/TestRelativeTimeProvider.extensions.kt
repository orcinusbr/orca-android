package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s a [TestRelativeTimeProvider]. **/
@Composable
internal fun rememberTestRelativeTimeProvider(): TestRelativeTimeProvider {
    return remember(::TestRelativeTimeProvider)
}

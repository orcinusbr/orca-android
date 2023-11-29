package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s a [RelativeTimeProvider]. */
@Composable
fun rememberRelativeTimeProvider(): RelativeTimeProvider {
  return remember(::Time4JRelativeTimeProvider)
}

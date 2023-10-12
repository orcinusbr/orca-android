package com.jeanbarrossilva.orca.platform.theme.reactivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Remembers a [BottomAreaAvailabilityNestedScrollConnection].
 *
 * @param listener [OnBottomAreaAvailabilityChangeListener] to be notified.
 */
@Composable
fun rememberBottomAreaAvailabilityNestedScrollConnection(
  listener: OnBottomAreaAvailabilityChangeListener
): BottomAreaAvailabilityNestedScrollConnection {
  return remember(listener) { BottomAreaAvailabilityNestedScrollConnection(listener) }
}

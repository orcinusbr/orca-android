package com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/**
 * Swipe-to-refresh behavior configuration for a [Timeline].
 *
 * @param isInProgress Whether the [Timeline] is currently being refreshed.
 * @param indicatorOffset Amount of [Dp]s by which the refresh indicator should be offset in the
 *   y-axis.
 * @param listener [Listener] to be notified of refreshes.
 */
@Immutable
data class Refresh(val isInProgress: Boolean, val indicatorOffset: Dp, val listener: Listener) {
  /**
   * Listens to refreshes.
   *
   * @see onRefresh
   */
  fun interface Listener {
    /** Callback run whenever the [Timeline] is refreshed. */
    fun onRefresh()
  }

  init {
    require(indicatorOffset.isSpecified) {
      "Cannot offset the refresh indicator by an unspecified amount of DPs."
    }
  }

  companion object {
    /** Never-active, no-op [Refresh]. */
    internal val Disabled = Refresh(isInProgress = false, indicatorOffset = 0.dp) {}

    /**
     * [Refresh] that remains active indefinitely.
     *
     * @see isInProgress
     */
    val Indefinite = Refresh(isInProgress = true, indicatorOffset = 0.dp) {}
  }
}

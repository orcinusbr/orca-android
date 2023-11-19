package com.jeanbarrossilva.orca.platform.autos.reactivity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * [NestedScrollConnection] that notifies the [listener] of scroll changes.
 *
 * @param listener [OnBottomAreaAvailabilityChangeListener] to be notified.
 */
class BottomAreaAvailabilityNestedScrollConnection
internal constructor(private val listener: OnBottomAreaAvailabilityChangeListener) :
  NestedScrollConnection {
  override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
    val currentOffsetY = listener.getCurrentOffsetY()
    val heightAsFloat = listener.height.toFloat()
    val changedOffsetY = (currentOffsetY - available.y).coerceIn(0f, heightAsFloat)
    listener.onBottomAreaAvailabilityChange(changedOffsetY)
    return Offset.Zero
  }

  companion object {
    /**
     * [BottomAreaAvailabilityNestedScrollConnection] with an empty
     * [OnBottomAreaAvailabilityChangeListener].
     *
     * @see OnBottomAreaAvailabilityChangeListener.empty
     */
    val empty =
      BottomAreaAvailabilityNestedScrollConnection(OnBottomAreaAvailabilityChangeListener.empty)
  }
}

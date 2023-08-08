package com.jeanbarrossilva.orca.platform.theme.reactivity

/**
 * Listens to changes on the availability of the utmost bottom portion of the displayed content.
 **/
interface OnBottomAreaAvailabilityChangeListener {
    /** Provides the height of the UI component in the bottom area.  **/
    val height: Int

    /** Provides the current offset in the Y-axis of the UI component in the bottom area. **/
    fun getCurrentOffsetY(): Float

    /**
     * Callback run whenever the availability of the bottom area is changed.
     *
     * @param offsetY Amount of offset in the Y-axis to be applied to a UI component in the bottom
     * area.
     **/
    fun onBottomAreaAvailabilityChange(offsetY: Float)

    companion object {
        /** No-op [OnBottomAreaAvailabilityChangeListener]. **/
        val empty = object : OnBottomAreaAvailabilityChangeListener {
            override val height = 0

            override fun getCurrentOffsetY(): Float {
                return 0f
            }

            override fun onBottomAreaAvailabilityChange(offsetY: Float) {
            }
        }
    }
}

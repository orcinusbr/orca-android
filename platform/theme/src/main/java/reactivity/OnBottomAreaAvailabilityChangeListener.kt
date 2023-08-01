package com.jeanbarrossilva.orca.platform.theme.reactivity

/**
 * Listens to changes on the availability of the utmost bottom portion of the displayed content.
 **/
interface OnBottomAreaAvailabilityChangeListener {
    /**
     * Callback run whenever the availability of the bottom area is changed.
     *
     * @param isAvailable Whether the bottom area is currently available.
     **/
    fun onBottomAreaAvailabilityChange(isAvailable: Boolean)

    companion object {
        /** No-op [OnBottomAreaAvailabilityChangeListener]. **/
        val empty = object : OnBottomAreaAvailabilityChangeListener {
            override fun onBottomAreaAvailabilityChange(isAvailable: Boolean) {
            }
        }
    }
}

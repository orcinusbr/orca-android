package com.jeanbarrossilva.mastodonte.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener

internal class MastodonteOnBottomAreaAvailabilityChangeListener :
    OnBottomAreaAvailabilityChangeListener {
    var isAvailable by mutableStateOf(false)

    override fun onBottomAreaAvailabilityChange(isAvailable: Boolean) {
        this.isAvailable = isAvailable
    }
}

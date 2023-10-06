package com.jeanbarrossilva.orca.app.module

import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.Injector

internal class AppModule(
    private val onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
) {
    fun inject() {
        Injector.inject {
            onBottomAreaAvailabilityChangeListener
        }
    }
}

package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TootDetailsModule(
    @Inject internal val tootProvider: Module.() -> TootProvider,
    @Inject internal val boundary: Module.() -> TootDetailsBoundary,
    @Inject internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.injector.Module

abstract class TootDetailsModule(
    tootProvider: Module.() -> TootProvider,
    boundary: Module.() -> TootDetailsBoundary
) : Module() {
    init {
        inject(tootProvider)
        inject(boundary)
    }
}

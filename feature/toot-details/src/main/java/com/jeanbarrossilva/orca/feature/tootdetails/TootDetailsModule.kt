package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TootDetailsModule(
    @Dependency private val tootProvider: Module.() -> TootProvider,
    @Dependency private val boundary: Module.() -> TootDetailsBoundary
) : Module()

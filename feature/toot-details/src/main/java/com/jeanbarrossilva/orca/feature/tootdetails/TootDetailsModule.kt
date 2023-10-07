package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TootDetailsModule(
    @Inject private val tootProvider: Module.() -> TootProvider,
    @Inject private val boundary: Module.() -> TootDetailsBoundary
) : Module()

package com.jeanbarrossilva.orca.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TermMutingModule(
    @Inject internal val termMuter: Module.() -> TermMuter,
    @Inject internal val boundary: Module.() -> TermMutingBoundary
) : Module()

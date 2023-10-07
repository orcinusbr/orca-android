package com.jeanbarrossilva.orca.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.Module

abstract class TermMutingModule : Module() {
    init {
        inject { termMuter() }
        inject { boundary() }
    }

    protected abstract fun Module.termMuter(): TermMuter

    protected abstract fun Module.boundary(): TermMutingBoundary
}

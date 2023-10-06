package com.jeanbarrossilva.orca.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TermMutingModule : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject { termMuter() }
        inject { boundary() }
    }

    protected abstract fun Injector.termMuter(): TermMuter

    protected abstract fun Injector.boundary(): TermMutingBoundary
}

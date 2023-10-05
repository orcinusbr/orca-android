package com.jeanbarrossilva.orca.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.Injectable
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class TermMutingModule : Module() {
    protected abstract val termMuter: Injectable<TermMuter>
    protected abstract val boundary: Injectable<TermMutingBoundary>

    override val dependencies: Scope.() -> Unit = {
        inject(termMuter)
        inject(boundary)
    }
}

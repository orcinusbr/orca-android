package com.jeanbarrossilva.orca.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.Injectable
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class SettingsModule : Module() {
    protected abstract val termMuter: Injectable<TermMuter>

    override val dependencies: Scope.() -> Unit = {
        inject(termMuter)
    }
}

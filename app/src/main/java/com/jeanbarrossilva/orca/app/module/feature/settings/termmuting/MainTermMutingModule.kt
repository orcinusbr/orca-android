package com.jeanbarrossilva.orca.app.module.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingBoundary
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTermMutingModule(private val navigator: Navigator) : TermMutingModule() {
    override fun Injector.termMuter(): TermMuter {
        return get()
    }

    override fun Injector.boundary(): TermMutingBoundary {
        return NavigatorTermMutingBoundary(navigator)
    }
}

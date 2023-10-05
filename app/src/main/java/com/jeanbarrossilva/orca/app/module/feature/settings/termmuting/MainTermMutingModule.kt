package com.jeanbarrossilva.orca.app.module.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingBoundary
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injectable

internal class MainTermMutingModule(navigator: Navigator) : TermMutingModule() {
    override val termMuter = Injectable<TermMuter> { get() }
    override val boundary =
        Injectable<TermMutingBoundary> {
            NavigatorTermMutingBoundary(navigator)
        }
}

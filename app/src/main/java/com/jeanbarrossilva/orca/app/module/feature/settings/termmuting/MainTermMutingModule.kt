package com.jeanbarrossilva.orca.app.module.feature.settings.termmuting

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTermMutingModule(private val navigator: Navigator) : TermMutingModule(
    termMuter = { Injector.from<CoreModule>().get() },
    { NavigatorTermMutingBoundary(navigator) }
)

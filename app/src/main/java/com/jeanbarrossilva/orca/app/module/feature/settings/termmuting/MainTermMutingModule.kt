package com.jeanbarrossilva.orca.app.module.feature.settings.termmuting

import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.termMuter
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTermMutingModule(private val navigator: Navigator) :
  TermMutingModule(
    { Injector.from<HttpModule>().termMuter() },
    { NavigatorTermMutingBoundary(navigator) }
  )

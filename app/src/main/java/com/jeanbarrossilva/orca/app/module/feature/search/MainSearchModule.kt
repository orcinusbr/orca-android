package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.app.module.core.instanceProvider
import com.jeanbarrossilva.orca.feature.search.SearchModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainSearchModule(navigator: Navigator) : SearchModule(
    { Injector.from<CoreModule>().instanceProvider().provide().profileSearcher },
    { NavigatorSearchBoundary(navigator) }
)

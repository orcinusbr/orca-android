package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal object SearchModule {
    fun inject(navigator: Navigator) {
        val boundary: SearchBoundary = NavigatorSearchBoundary(navigator)
        Injector.inject(boundary)
    }
}

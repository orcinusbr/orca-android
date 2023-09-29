package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.module.Module

internal class SearchModule(navigator: Navigator) : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject<SearchBoundary> {
            NavigatorSearchBoundary(navigator)
        }
    }
}

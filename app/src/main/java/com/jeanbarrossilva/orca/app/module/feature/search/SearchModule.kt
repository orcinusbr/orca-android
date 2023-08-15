package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun SearchModule(navigator: Navigator): Module {
    return module {
        single<SearchBoundary> {
            NavigatorSearchBoundary(navigator)
        }
    }
}

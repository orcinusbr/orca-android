package com.jeanbarrossilva.orca.feature.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.std.injector.Module

abstract class SearchModule(
    searcher: Module.() -> ProfileSearcher,
    boundary: Module.() -> SearchBoundary
) : Module() {
    init {
        inject(searcher)
        inject(boundary)
    }
}

package com.jeanbarrossilva.orca.feature.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class SearchModule(
    @Dependency private val searcher: Module.() -> ProfileSearcher,
    @Dependency private val boundary: Module.() -> SearchBoundary
) : Module()

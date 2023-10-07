package com.jeanbarrossilva.orca.feature.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class SearchModule(
    @Inject private val searcher: Module.() -> ProfileSearcher,
    @Inject private val boundary: Module.() -> SearchBoundary
) : Module()

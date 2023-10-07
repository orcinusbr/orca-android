package com.jeanbarrossilva.orca.feature.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class FeedModule(
    @Dependency private val feedProvider: Module.() -> FeedProvider,
    @Dependency private val tootProvider: Module.() -> TootProvider,
    @Dependency private val boundary: Module.() -> FeedBoundary
) : Module()

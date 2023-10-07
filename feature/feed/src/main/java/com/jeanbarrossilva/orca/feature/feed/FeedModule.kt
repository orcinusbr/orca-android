package com.jeanbarrossilva.orca.feature.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.injector.Module

abstract class FeedModule(
    feedProvider: Module.() -> FeedProvider,
    tootProvider: Module.() -> TootProvider,
    boundary: Module.() -> FeedBoundary
) : Module() {
    init {
        inject(feedProvider)
        inject(tootProvider)
        inject(boundary)
    }
}

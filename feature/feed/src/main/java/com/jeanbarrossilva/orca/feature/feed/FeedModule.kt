package com.jeanbarrossilva.orca.feature.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class FeedModule(
    @Inject private val feedProvider: Module.() -> FeedProvider,
    @Inject private val tootProvider: Module.() -> TootProvider,
    @Inject private val boundary: Module.() -> FeedBoundary,
    @Inject private val onBottomAreaAvailabilityChangeListener: Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

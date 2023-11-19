package com.jeanbarrossilva.orca.feature.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class FeedModule(
  @Inject internal val feedProvider: Module.() -> FeedProvider,
  @Inject internal val tootProvider: Module.() -> TootProvider,
  @Inject internal val boundary: Module.() -> FeedBoundary,
  @Inject
  internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

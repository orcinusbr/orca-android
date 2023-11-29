package com.jeanbarrossilva.orca.feature.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class FeedModule(
  @Inject internal val feedProvider: Module.() -> FeedProvider,
  @Inject internal val postProvider: Module.() -> PostProvider,
  @Inject internal val boundary: Module.() -> FeedBoundary,
  @Inject
  internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

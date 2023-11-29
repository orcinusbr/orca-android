package com.jeanbarrossilva.orca.feature.postdetails

import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class PostDetailsModule(
  @Inject internal val postProvider: Module.() -> PostProvider,
  @Inject internal val boundary: Module.() -> PostDetailsBoundary,
  @Inject
  internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

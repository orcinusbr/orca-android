package com.jeanbarrossilva.orca.feature.profiledetails

import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class ProfileDetailsModule(
  @Inject internal val profileProvider: Module.() -> ProfileProvider,
  @Inject internal val postProvider: Module.() -> PostProvider,
  @Inject internal val boundary: Module.() -> ProfileDetailsBoundary,
  @Inject
  internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

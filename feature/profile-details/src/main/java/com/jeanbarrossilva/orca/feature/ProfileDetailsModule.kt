package com.jeanbarrossilva.orca.feature

import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class ProfileDetailsModule(
  @Inject internal val profileProvider: Module.() -> ProfileProvider,
  @Inject internal val tootProvider: Module.() -> TootProvider,
  @Inject internal val boundary: Module.() -> ProfileDetailsBoundary,
  @Inject
  internal val onBottomAreaAvailabilityChangeListener:
    Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

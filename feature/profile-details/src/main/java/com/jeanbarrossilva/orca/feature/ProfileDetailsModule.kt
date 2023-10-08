package com.jeanbarrossilva.orca.feature

import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class ProfileDetailsModule(
    @Inject private val profileProvider: Module.() -> ProfileProvider,
    @Inject private val tootProvider: Module.() -> TootProvider,
    @Inject private val boundary: Module.() -> ProfileDetailsBoundary,
    @Inject private val onBottomAreaAvailabilityChangeListener: Module.() -> OnBottomAreaAvailabilityChangeListener
) : Module()

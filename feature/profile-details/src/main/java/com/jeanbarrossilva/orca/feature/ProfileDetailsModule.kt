package com.jeanbarrossilva.orca.feature

import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class ProfileDetailsModule(
    @Dependency private val profileProvider: Module.() -> ProfileProvider,
    @Dependency private val tootProvider: Module.() -> TootProvider,
    @Dependency private val boundary: Module.() -> ProfileDetailsBoundary
) : Module()

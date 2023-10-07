package com.jeanbarrossilva.orca.feature

import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.std.injector.Module

abstract class ProfileDetailsModule(
    profileProvider: Module.() -> ProfileProvider,
    tootProvider: Module.() -> TootProvider,
    boundary: Module.() -> ProfileDetailsBoundary
) : Module() {
    init {
        inject(profileProvider)
        inject(tootProvider)
        inject(boundary)
    }
}

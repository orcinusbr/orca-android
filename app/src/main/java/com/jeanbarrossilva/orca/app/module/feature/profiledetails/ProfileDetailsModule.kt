package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.module.Module

internal class ProfileDetailsModule(activity: NavigationActivity) : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject<ProfileDetailsBoundary> {
            NavigatorProfileDetailsBoundary(activity, activity.navigator)
        }
    }
}

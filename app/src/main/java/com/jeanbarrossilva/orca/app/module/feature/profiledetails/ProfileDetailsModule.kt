package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal object ProfileDetailsModule {
    fun inject(activity: NavigationActivity) {
        val boundary: ProfileDetailsBoundary =
            NavigatorProfileDetailsBoundary(activity, activity.navigator)
        Injector.inject(boundary)
    }
}

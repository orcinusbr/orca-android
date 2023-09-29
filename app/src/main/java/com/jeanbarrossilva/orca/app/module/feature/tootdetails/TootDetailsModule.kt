package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal object TootDetailsModule {
    fun inject(activity: NavigationActivity) {
        val boundary: TootDetailsBoundary =
            NavigatorTootDetailsBoundary(activity, activity.navigator)
        Injector.inject(boundary)
    }
}

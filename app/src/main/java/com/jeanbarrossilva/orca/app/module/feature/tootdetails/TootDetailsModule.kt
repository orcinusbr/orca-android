package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.module.Module

internal class TootDetailsModule(activity: NavigationActivity) : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject<TootDetailsBoundary> {
            NavigatorTootDetailsBoundary(activity, activity.navigator)
        }
    }
}

package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.module.Module

internal class FeedModule(activity: NavigationActivity) : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject<FeedBoundary> {
            NavigatorFeedBoundary(activity, activity.navigator)
        }
    }
}

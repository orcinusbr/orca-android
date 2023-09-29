package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

object FeedModule {
    fun inject(activity: NavigationActivity) {
        val boundary: FeedBoundary = NavigatorFeedBoundary(activity, activity.navigator)
        Injector.inject(boundary)
    }
}

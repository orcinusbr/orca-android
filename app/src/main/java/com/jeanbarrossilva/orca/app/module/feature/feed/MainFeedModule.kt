package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: OrcaActivity) : FeedModule(
    { Injector.from<HttpModule>().instanceProvider().provide().feedProvider },
    { Injector.from<HttpModule>().instanceProvider().provide().tootProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

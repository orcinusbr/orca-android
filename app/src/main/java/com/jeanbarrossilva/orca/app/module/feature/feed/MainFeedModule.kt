package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.app.module.core.instanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: OrcaActivity) : FeedModule(
    { Injector.from<CoreModule>().instanceProvider().provide().feedProvider },
    { Injector.from<CoreModule>().instanceProvider().provide().tootProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

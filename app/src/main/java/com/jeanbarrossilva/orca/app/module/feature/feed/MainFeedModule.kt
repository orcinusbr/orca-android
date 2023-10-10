package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: OrcaActivity) : FeedModule(
    { Injector.from<HttpModule>().get<InstanceProvider>().provide().feedProvider },
    { Injector.from<HttpModule>().get<InstanceProvider>().provide().tootProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: OrcaActivity) :
  FeedModule(
    { Injector.from<CoreModule>().instanceProvider().provide().feedProvider },
    { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
  )

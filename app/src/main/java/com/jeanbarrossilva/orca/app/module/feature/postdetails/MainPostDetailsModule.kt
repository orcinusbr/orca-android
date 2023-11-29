package com.jeanbarrossilva.orca.app.module.feature.postdetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainPostDetailsModule(activity: OrcaActivity) :
  PostDetailsModule(
    { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    { NavigatorPostDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
  )

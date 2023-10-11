package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.app.module.core.instanceProvider
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTootDetailsModule(activity: OrcaActivity) : TootDetailsModule(
    { Injector.from<CoreModule>().instanceProvider().provide().tootProvider },
    { NavigatorTootDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

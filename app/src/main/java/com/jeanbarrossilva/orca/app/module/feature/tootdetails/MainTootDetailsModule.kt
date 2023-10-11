package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTootDetailsModule(activity: OrcaActivity) : TootDetailsModule(
    { Injector.from<HttpModule>().instanceProvider().provide().tootProvider },
    { NavigatorTootDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

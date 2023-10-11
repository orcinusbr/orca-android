package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainProfileDetailsModule(activity: OrcaActivity) : ProfileDetailsModule(
    { Injector.from<HttpModule>().instanceProvider().provide().profileProvider },
    { Injector.from<HttpModule>().instanceProvider().provide().tootProvider },
    { NavigatorProfileDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

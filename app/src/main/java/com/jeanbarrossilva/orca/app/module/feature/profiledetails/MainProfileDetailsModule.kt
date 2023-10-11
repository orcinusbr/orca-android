package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.app.module.core.instanceProvider
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainProfileDetailsModule(activity: OrcaActivity) : ProfileDetailsModule(
    { Injector.from<CoreModule>().instanceProvider().provide().profileProvider },
    { Injector.from<CoreModule>().instanceProvider().provide().tootProvider },
    { NavigatorProfileDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

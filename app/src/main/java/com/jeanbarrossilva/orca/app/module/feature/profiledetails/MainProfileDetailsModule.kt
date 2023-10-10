package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainProfileDetailsModule(activity: OrcaActivity) : ProfileDetailsModule(
    { Injector.from<HttpModule>().get<InstanceProvider>().provide().profileProvider },
    { Injector.from<HttpModule>().get<InstanceProvider>().provide().tootProvider },
    { NavigatorProfileDetailsBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
)

package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainProfileDetailsModule(activity: NavigationActivity) : ProfileDetailsModule(
    { Injector.from<CoreModule>().get<InstanceProvider>().provide().profileProvider },
    { Injector.from<CoreModule>().get<InstanceProvider>().provide().tootProvider },
    { NavigatorProfileDetailsBoundary(activity, activity.navigator) }
)

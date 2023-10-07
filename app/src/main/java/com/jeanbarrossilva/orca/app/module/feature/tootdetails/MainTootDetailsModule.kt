package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainTootDetailsModule(activity: NavigationActivity) : TootDetailsModule(
    { Injector.from<CoreModule>().get<InstanceProvider>().provide().tootProvider },
    { NavigatorTootDetailsBoundary(activity, activity.navigator) }
)

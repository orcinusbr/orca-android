package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: NavigationActivity) : FeedModule(
    { Injector.from<CoreModule>().get<InstanceProvider>().provide().feedProvider },
    { Injector.from<CoreModule>().get<InstanceProvider>().provide().tootProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) }
)

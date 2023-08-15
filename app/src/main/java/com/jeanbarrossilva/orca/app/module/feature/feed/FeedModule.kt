package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun FeedModule(navigator: Navigator): Module {
    return module {
        single<FeedBoundary> {
            NavigatorFeedBoundary(androidContext(), navigator)
        }
    }
}

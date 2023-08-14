package com.jeanbarrossilva.orca.app.module.feature.feed

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun FeedModule(fragmentManager: FragmentManager, @IdRes containerID: Int): Module {
    return module {
        single<FeedBoundary> {
            FragmentManagerFeedBoundary(androidContext(), fragmentManager, containerID)
        }
    }
}

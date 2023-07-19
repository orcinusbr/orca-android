package com.jeanbarrossilva.mastodonte.app.feature.feed

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.feed.FeedBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun FeedModule(fragmentManager: FragmentManager, @IdRes containerID: Int): Module {
    return module {
        single<FeedBoundary> {
            FragmentManagerFeedBoundary(fragmentManager, containerID)
        }
    }
}

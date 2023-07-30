package com.jeanbarrossilva.mastodonte.app.module.feature.feed

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.feed.FeedBoundary
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MainFeedModule(fragmentManager: FragmentManager, @IdRes containerID: Int): Module {
    return FeedModule {
        FragmentManagerFeedBoundary(fragmentManager, containerID)
    }
}

@Suppress("FunctionName")
internal fun FeedModule(boundary: Definition<FeedBoundary>): Module {
    return module {
        single(definition = boundary)
    }
}

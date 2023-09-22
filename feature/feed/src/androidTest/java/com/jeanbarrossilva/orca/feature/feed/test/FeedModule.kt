package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun FeedModule(): Module {
    return module {
        single { Instance.sample }
        single<FeedBoundary> { TestFeedBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}

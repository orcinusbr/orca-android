package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun FeedModule(): Module {
    return module {
        single<FeedProvider> { SampleFeedProvider }
        single<TootProvider> { SampleTootProvider }
        single<FeedBoundary> { TestFeedBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}

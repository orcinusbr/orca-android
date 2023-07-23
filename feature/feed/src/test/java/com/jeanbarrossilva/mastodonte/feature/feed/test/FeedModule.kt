package com.jeanbarrossilva.mastodonte.feature.feed.test

import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.mastodonte.core.sample.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.feature.feed.FeedBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun FeedModule(): Module {
    return module {
        single<FeedProvider> { SampleFeedProvider }
        single<TootProvider> { SampleTootProvider }
        single<FeedBoundary> { TestFeedBoundary() }
    }
}

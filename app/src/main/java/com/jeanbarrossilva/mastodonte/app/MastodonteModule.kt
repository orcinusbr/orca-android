package com.jeanbarrossilva.mastodonte.app

import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MastodonteModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    return module {
        single<FeedProvider> { SampleFeedProvider }
        single<ProfileProvider> { SampleProfileProvider }
        single<TootProvider> { SampleTootProvider }
        single { onBottomAreaAvailabilityChangeListener }
    }
}

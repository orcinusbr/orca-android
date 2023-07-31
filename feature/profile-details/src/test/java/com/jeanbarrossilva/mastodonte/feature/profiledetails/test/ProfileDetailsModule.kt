package com.jeanbarrossilva.mastodonte.feature.profiledetails.test

import com.jeanbarrossilva.mastodonte.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun ProfileDetailsModule(): Module {
    return module {
        single<ProfileProvider> { SampleProfileProvider }
        single<TootProvider> { SampleTootProvider }
        single<ProfileDetailsBoundary> { TestProfileDetailsBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}

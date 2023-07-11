package com.jeanbarrossilva.mastodon.feature.profiledetails.test

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun ProfileDetailsModule(): Module {
    return module {
        single<ProfileProvider> { SampleProfileDao }
        single<ProfileDetailsBoundary> { TestProfileDetailsBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}

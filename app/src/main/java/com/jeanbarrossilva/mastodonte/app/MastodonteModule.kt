package com.jeanbarrossilva.mastodonte.app

import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.SampleTootDao
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MastodonteModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    return module {
        single<ProfileRepository> { SampleProfileDao }
        single<TootRepository> { SampleTootDao }
        single { onBottomAreaAvailabilityChangeListener }
    }
}

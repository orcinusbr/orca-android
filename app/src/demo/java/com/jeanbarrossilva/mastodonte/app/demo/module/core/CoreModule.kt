package com.jeanbarrossilva.mastodonte.app.demo.module.core

import com.jeanbarrossilva.mastodonte.app.module.core.CoreModule
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.mastodonte.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.test.TestActorProvider
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun DemoCoreModule(): Module {
    val actorProvider = TestActorProvider()
    return CoreModule(
        { SampleAuthenticator(actorProvider) },
        { AuthenticationLock(authenticator = get(), actorProvider) },
        { SampleFeedProvider },
        { SampleProfileProvider },
        { SampleTootProvider }
    ) {
        OnBottomAreaAvailabilityChangeListener.empty
    }
}

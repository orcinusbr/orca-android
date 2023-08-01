package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.test.TestActorProvider
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
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

package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.auth.actor.SampleActorProvider
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample

/** [Instance] returned by [sample]. **/
private val sampleInstance = object : Instance<SampleAuthenticator>() {
    override val domain = Domain.sample
    override val authenticator = SampleAuthenticator(SampleActorProvider)
    override val authenticationLock = AuthenticationLock(authenticator, SampleActorProvider)
    override val feedProvider = SampleFeedProvider
    override val profileProvider = SampleProfileProvider
    override val profileSearcher = SampleProfileSearcher
    override val tootProvider = SampleTootProvider
}

/** Sample [Instance]. **/
val Instance.Companion.sample
    get() = sampleInstance

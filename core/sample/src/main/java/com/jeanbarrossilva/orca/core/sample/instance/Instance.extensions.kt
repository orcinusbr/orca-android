package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.core.test.TestActorProvider

/** [Instance] returned by [sample]. **/
private val sampleInstance = object : Instance<SampleAuthenticator>() {
    /**
     * [TestActorProvider] that will provide an [Actor] to both the [authenticator] and the
     * [authenticationLock].
     **/
    private val actorProvider
        get() = TestActorProvider()

    override val domain = Domain.sample
    override val authenticator = SampleAuthenticator(actorProvider)
    override val authenticationLock = AuthenticationLock(authenticator, actorProvider)
    override val feedProvider = SampleFeedProvider
    override val profileProvider = SampleProfileProvider
    override val profileSearcher = SampleProfileSearcher
    override val tootProvider = SampleTootProvider
}

/** Sample [Instance]. **/
val Instance.Companion.sample
    get() = sampleInstance

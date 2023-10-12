package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample

/** [Instance] returned by [sample]. */
private val sampleInstance =
  object : Instance<Authenticator>() {
    override val domain = Domain.sample
    override val authenticator: Authenticator = SampleAuthenticator
    override val authenticationLock = AuthenticationLock(authenticator, ActorProvider.sample)
    override val feedProvider = SampleFeedProvider
    override val profileProvider = SampleProfileProvider
    override val profileSearcher = SampleProfileSearcher
    override val tootProvider = SampleTootProvider
  }

/** Sample [Instance]. */
val Instance.Companion.sample
  get() = sampleInstance

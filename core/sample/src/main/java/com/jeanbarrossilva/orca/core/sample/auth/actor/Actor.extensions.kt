package com.jeanbarrossilva.orca.core.sample.auth.actor

import com.jeanbarrossilva.orca.core.auth.actor.Actor

/** [Actor.Authenticated] returned by [sample]. */
private val authenticatedActorSample = Actor.Authenticated("sample-id", "sample-access-token")

/** Sample [authenticated][Actor.Authenticated] [Actor]. */
val Actor.Authenticated.Companion.sample
  get() = authenticatedActorSample

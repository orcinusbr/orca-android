package com.jeanbarrossilva.orca.core.sample.auth

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample

/**
 * [Authenticator] that provides a sample [Actor].
 *
 * @param actorProvider [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor]
 *   will be sent to be remembered when authentication occurs.
 */
internal object SampleAuthenticator : Authenticator() {
  override val authorizer: Authorizer = Authorizer.sample
  override val actorProvider = ActorProvider.sample

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    return Actor.Authenticated.sample
  }
}

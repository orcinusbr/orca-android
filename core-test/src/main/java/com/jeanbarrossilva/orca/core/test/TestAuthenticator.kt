package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.Actor

/**
 * [Authenticator] that switches the [Actor] locally on authentication.
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 * @param actorProvider [TestActorProvider] to which the [authenticated][Actor.Authenticated]
 *   [Actor] will be sent to be remembered when authentication occurs.
 * @param onOnAuthenticate Operation to be performed when [onAuthenticate] is called.
 * @see currentActor
 * @see switchCurrentActor
 */
class TestAuthenticator(
  override val authorizer: TestAuthorizer = TestAuthorizer(),
  override val actorProvider: TestActorProvider = TestActorProvider(),
  private val onOnAuthenticate: suspend (authorizationCode: String) -> Unit = {}
) : Authenticator() {
  /**
   * [Actor] to be switched on [onAuthenticate].
   *
   * @see switchCurrentActor
   */
  private var currentActor: Actor = Actor.Unauthenticated

  /**
   * [Authenticated][Actor.Authenticated] [Actor] to switch to when the [currentActor] is
   * [unauthenticated][Actor.Unauthenticated].
   */
  private val authenticatedActor = Actor.Authenticated("test-id", "test-access-token")

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    onOnAuthenticate(authorizationCode)
    switchCurrentActor()
    return currentActor
  }

  /**
   * Switches the [currentActor] to an [authenticated][Actor.Authenticated] one if it's
   * [unauthenticated][Actor.Unauthenticated] or vice-versa.
   */
  private fun switchCurrentActor() {
    currentActor =
      when (currentActor) {
        is Actor.Unauthenticated -> authenticatedActor
        is Actor.Authenticated -> Actor.Unauthenticated
      }
  }
}

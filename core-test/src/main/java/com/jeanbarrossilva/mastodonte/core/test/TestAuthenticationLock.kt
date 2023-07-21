package com.jeanbarrossilva.mastodonte.core.test

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor

/**
 * [AuthenticationLock] with test-specific default structures.
 *
 * @param authorizer [TestAuthorizer] by which [authenticator]'s authentication will be authorized.
 * @param authenticator [TestAuthenticator] through which the [Actor] will be authenticated if it
 * isn't and [unlock][AuthenticationLock.unlock] is called.
 * @param actorProvider [TestActorProvider] whose provided [Actor] will be ensured to be either
 * [unauthenticated][Actor.Unauthenticated] or [authenticated][Actor.Authenticated].
 **/
@Suppress("FunctionName")
fun TestAuthenticationLock(
    authorizer: TestAuthorizer = TestAuthorizer(),
    actorProvider: TestActorProvider = TestActorProvider(),
    authenticator: TestAuthenticator = TestAuthenticator(actorProvider)
): AuthenticationLock {
    return AuthenticationLock(authorizer, authenticator, actorProvider)
}

package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication

import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.MastodonAuthorizer
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MastodonAuthenticator(
    override val authorizer: MastodonAuthorizer,
    override val actorProvider: ActorProvider
) : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        val authentication = Mastodon
            .HttpClient
            .get("/oauth/token") {
                parameter("grant_type", "authorization_code")
                parameter("code", authorizationCode)
                parameter("client_id", Mastodon.CLIENT_ID)
                parameter("client_secret", Mastodon.clientSecret)
                parameter("redirect_uri", "mastodonte://authenticated")
                parameter("scope", Mastodon.scopes)
            }
            .body<Authentication>()
        return Actor.Authenticated(authentication.accessToken)
    }
}

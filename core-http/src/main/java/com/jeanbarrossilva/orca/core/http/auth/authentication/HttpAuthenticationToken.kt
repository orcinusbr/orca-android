package com.jeanbarrossilva.orca.core.http.auth.authentication

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

/**
 * Structure returned by the Mastodon API that holds the access token that's been given when
 * authorization was successfully granted to the user.
 *
 * @param accessToken Token that gives Orca user-level access to the API resources.
 **/
@Serializable
internal data class HttpAuthenticationToken(val accessToken: String) {
    /**
     * Converts this [HttpAuthenticationToken] into an [authenticated][Actor.Authenticated] [Actor].
     *
     * @param client [HttpClient] by which an [HttpRequest] to retrieve the user's ID will be
     * performed.
     **/
    suspend fun toActor(client: HttpClient): Actor.Authenticated {
        val id = client
            .get("/api/v1/accounts/verify_credentials") { bearerAuth(accessToken) }
            .body<HttpAuthenticationVerification>()
            .id
        return Actor.Authenticated(id, accessToken)
    }
}

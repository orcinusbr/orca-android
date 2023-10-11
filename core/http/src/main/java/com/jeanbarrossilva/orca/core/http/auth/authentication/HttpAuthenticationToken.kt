package com.jeanbarrossilva.orca.core.http.auth.authentication

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
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
     **/
    suspend fun toActor(): Actor.Authenticated {
        val id = (Injector.from<HttpModule>().instanceProvider().provide() as SomeHttpInstance)
            .client
            .get("/api/v1/accounts/verify_credentials") { bearerAuth(accessToken) }
            .body<HttpAuthenticationVerification>()
            .id
        return Actor.Authenticated(id, accessToken)
    }
}

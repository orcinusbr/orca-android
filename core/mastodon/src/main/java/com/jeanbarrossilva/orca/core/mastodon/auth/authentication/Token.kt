package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.CredentialAccount
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
internal data class Token(val accessToken: String) {
    suspend fun toActor(): Actor.Authenticated {
        val id = MastodonHttpClient
            .get("/api/v1/accounts/verify_credentials") { bearerAuth(accessToken) }
            .body<CredentialAccount>()
            .id
        return Actor.Authenticated(id, accessToken)
    }
}

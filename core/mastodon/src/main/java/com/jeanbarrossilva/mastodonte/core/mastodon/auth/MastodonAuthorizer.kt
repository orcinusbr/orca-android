package com.jeanbarrossilva.mastodonte.core.mastodon.auth

import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url

class MastodonAuthorizer : Authorizer() {
    class UnavailableAuthorizationCodeException internal constructor() :
        IllegalStateException("Authorization code was not provided by Mastodon.")

    override suspend fun authorize(): String {
        return Mastodon
            .HttpClient
            .get("/oauth/authorize") {
                parameter("response_type", "code")
                parameter("client_id", Mastodon.CLIENT_ID)
                parameter("redirect_ui", "mastodonte://authorized")
                parameter("scope", Mastodon.scopes)
            }
            .bodyAsText()
            .let(::Url)
            .parameters["code"]
            ?: throw UnavailableAuthorizationCodeException()
    }
}

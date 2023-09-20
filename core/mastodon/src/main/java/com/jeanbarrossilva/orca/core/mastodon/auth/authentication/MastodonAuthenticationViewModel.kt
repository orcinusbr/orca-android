package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticationViewModel
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.R
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters

class MastodonAuthenticationViewModel private constructor(
    application: Application,
    private val authorizationCode: String
) : HttpAuthenticationViewModel(application) {
    override suspend fun request(): Actor {
        val application = getApplication<Application>()
        val scheme = application.getString(R.string.mastodon_scheme)
        val redirectUri = application.getString(R.string.mastodon_redirect_uri, scheme)
        return MastodonHttpClient
            .submitForm(
                "/oauth/token",
                Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", authorizationCode)
                    append("client_id", Mastodon.CLIENT_ID)
                    append("client_secret", Mastodon.clientSecret)
                    append("redirect_uri", redirectUri)
                    append("scope", Mastodon.scopes)
                }
            )
            .body<Token>()
            .toActor()
    }

    companion object {
        fun createFactory(application: Application, authorizationCode: String):
            ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MastodonAuthenticationViewModel(application, authorizationCode)
                }
            }
        }
    }
}

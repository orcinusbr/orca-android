package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.R
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.Token
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import kotlinx.coroutines.launch

internal class AuthenticationViewModel private constructor(
    application: Application,
    private val authorizationCode: String
) : AndroidViewModel(application) {
    fun withActor(action: (Actor) -> Unit) {
        viewModelScope.launch {
            request().body<Token>().toActor().run(action)
        }
    }

    private suspend fun request(): HttpResponse {
        val application = getApplication<Application>()
        val scheme = application.getString(R.string.mastodon_scheme)
        val redirectUri = application.getString(R.string.mastodon_redirect_uri, scheme)
        val scopes = Mastodon.getScopes(Mastodon.ScopeProvisionSite.FORM)
        return MastodonHttpClient.submitForm(
            "/oauth/token",
            Parameters.build {
                append("grant_type", "authorization_code")
                append("code", authorizationCode)
                append("client_id", Mastodon.CLIENT_ID)
                append("client_secret", Mastodon.clientSecret)
                append("redirect_uri", redirectUri)
                append("scope", scopes)
            }
        )
    }

    companion object {
        fun createFactory(
            application: Application,
            authorizationCode: String
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AuthenticationViewModel(application, authorizationCode)
                }
            }
        }
    }
}

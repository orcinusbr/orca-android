package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.R
import io.ktor.http.Url

internal class AuthorizationViewModel private constructor(application: Application) :
    AndroidViewModel(application) {
    val url: Url
        get() {
            val application = getApplication<Application>()
            val scheme = application.getString(R.string.mastodon_scheme)
            val redirectUri =
                application.getString(R.string.mastodon_authorization_redirect_uri, scheme)
            return Url(
                "${Mastodon.baseUrl}/oauth/authorize?" +
                    "response_type=code&" +
                    "client_id=${Mastodon.CLIENT_ID}&" +
                    "redirect_uri=$redirectUri&" +
                    "scope=${Mastodon.scopes}"
            )
        }

    companion object {
        fun createFactory(application: Application): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AuthorizationViewModel(application)
                }
            }
        }
    }
}

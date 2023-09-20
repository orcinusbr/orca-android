package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizationViewModel
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.R
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

class MastodonAuthorizationViewModel private constructor(application: Application) :
    HttpAuthorizationViewModel(
        application,
        lazy {
            URLBuilder()
                .takeFrom(Mastodon.baseUrl)
                .appendPathSegments("oauth", "authorize")
                .apply {
                    with(application.getString(R.string.mastodon_scheme)) {
                        parameters["response_type"] = "code"
                        parameters["client_id"] = Mastodon.CLIENT_ID
                        parameters["redirect_uri"] =
                            application.getString(R.string.mastodon_redirect_uri, this)
                        parameters["scope"] = Mastodon.scopes
                    }
                }
                .build()
        }
    ) {
    companion object {
        fun createFactory(application: Application): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MastodonAuthorizationViewModel(application)
                }
            }
        }
    }
}

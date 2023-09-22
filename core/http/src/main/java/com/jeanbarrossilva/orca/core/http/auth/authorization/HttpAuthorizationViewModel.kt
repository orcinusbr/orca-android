package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.http.R
import com.jeanbarrossilva.orca.core.http.auth.Mastodon
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import org.koin.android.ext.android.get

/**
 * [AndroidViewModel] that provides the [url] to be opened in the browser for authenticating the
 * user.
 *
 * @param application [Application] that allows [Context]-specific behavior.
 **/
internal class HttpAuthorizationViewModel private constructor(application: Application) :
    AndroidViewModel(application) {
    /** [Url] to be opened in order to authenticate. **/
    val url by lazy {
        URLBuilder()
            .takeFrom(application.get<Url>())
            .appendPathSegments("oauth", "authorize")
            .apply {
                with(application.getString(R.string.scheme)) {
                    parameters["response_type"] = "code"
                    parameters["client_id"] = Mastodon.CLIENT_ID
                    parameters["redirect_uri"] = application.getString(R.string.redirect_uri, this)
                    parameters["scope"] = Mastodon.SCOPES
                }
            }
            .build()
    }

    companion object {
        /**
         * Creates a [ViewModelProvider.Factory] that provides an [HttpAuthorizationViewModel].
         *
         * @param application [Application] for [Context]-specific behavior.
         **/
        fun createFactory(application: Application): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    HttpAuthorizationViewModel(application)
                }
            }
        }
    }
}

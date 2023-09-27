package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.R
import com.jeanbarrossilva.orca.core.http.auth.Mastodon
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [AndroidViewModel] that provides the [url] to be opened in the browser for authenticating the
 * user.
 *
 * @param application [Application] that allows [Context]-specific behavior.
 * @param onAccessTokenRequestListener [OnAccessTokenRequestListener] to be notified when an access
 * token is requested.
 **/
internal class HttpAuthorizationViewModel private constructor(
    application: Application,
    private val onAccessTokenRequestListener: OnAccessTokenRequestListener
) : AndroidViewModel(application) {
    /** [MutableStateFlow] to which the user's username will be emitted. **/
    private val usernameMutableFlow = MutableStateFlow("")

    /**
     * [MutableStateFlow] to which the [String] version of the [Domain] of the user's [Instance]
     * will be emitted.
     **/
    private val instanceMutableFlow = MutableStateFlow("")

    /** [Url] to be opened in order to authenticate. **/
    internal val url by lazy {
        URLBuilder()
            .takeFrom(HttpBridge.instance.url)
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

    /** [StateFlow] version of [usernameMutableFlow]. **/
    val usernameFlow = usernameMutableFlow.asStateFlow()

    /** [StateFlow] version of [instanceMutableFlow]. **/
    val instanceFlow = instanceMutableFlow.asStateFlow()

    /**
     * Changes the username to the given [username].
     *
     * @param username Username to change the current one to.
     **/
    fun setUsername(username: String) {
        usernameMutableFlow.value = username
    }

    /**
     * Changes the instance [Domain] to the given [instance].
     *
     * @param instance [String] version of the [Domain] of the [Instance] to change the current one
     * to.
     **/
    fun setInstance(instance: String) {
        instanceMutableFlow.value = instance
    }

    /**
     * Authorizes the user by taking them to their [Instance]'s webpage for their credentials to be
     * inserted.
     **/
    fun authorize() {
        onAccessTokenRequestListener.onAccessTokenRequest()
    }

    companion object {
        /**
         * Creates a [ViewModelProvider.Factory] that provides an [HttpAuthorizationViewModel].
         *
         * @param application [Application] for [Context]-specific behavior.
         * @param onAccessTokenRequestListener [OnAccessTokenRequestListener] to be notified when an
         * access token is requested.
         **/
        fun createFactory(
            application: Application,
            onAccessTokenRequestListener: OnAccessTokenRequestListener
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    HttpAuthorizationViewModel(application, onAccessTokenRequestListener)
                }
            }
        }
    }
}

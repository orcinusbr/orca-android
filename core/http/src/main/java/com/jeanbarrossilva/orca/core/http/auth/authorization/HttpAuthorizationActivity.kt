package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.instance.ContextualHttpInstance
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import io.ktor.http.Url

/**
 * [ComposableActivity] that visually notifies the user of the background authorization process that
 * takes place when this is created and automatically finishes itself when it's done.
 **/
class HttpAuthorizationActivity internal constructor() : ComposableActivity() {
    /**
     * [HttpAuthorizationViewModel] from which the [Url] to the authorization webpage will be
     * obtained.
     **/
    private val viewModel by viewModels<HttpAuthorizationViewModel> {
        HttpAuthorizationViewModel.createFactory(application)
    }

    /**
     * [IllegalStateException] thrown if the [HttpAuthorizationActivity] has been started from a
     * deep link and an access token isn't provided.
     **/
    class UnprovidedAccessTokenException internal constructor() : IllegalStateException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestAccessTokenOrSendToAuthorizer()
    }

    @Composable
    override fun Content() {
        HttpAuthorization()
    }

    /**
     * Sends the access token that's been successfully retrieved from the deep link through which
     * this [HttpAuthorizationActivity] was started to the [HttpAuthorizer] or requests the access
     * token if it's been started directly by Orca.
     *
     * @throws UnprovidedAccessTokenException If this [HttpAuthorizationActivity] has been started
     * from a deep link and an access token hasn't been provided.
     **/
    @Throws(UnprovidedAccessTokenException::class)
    private fun requestAccessTokenOrSendToAuthorizer() {
        intent.data?.let(::sendAccessTokenToAuthorizer) ?: requestAccessToken()
    }

    /**
     * Sends the access token that might be in the [deepLink] to the [HttpAuthorizer].
     *
     * @param deepLink [Uri] from which this [HttpAuthorizationActivity] was started after
     * requesting for the user to be authorized.
     * @throws UnprovidedAccessTokenException If an access token hasn't been provided.
     **/
    @Throws(UnprovidedAccessTokenException::class)
    private fun sendAccessTokenToAuthorizer(deepLink: Uri) {
        val accessToken =
            deepLink.getQueryParameter("code") ?: throw UnprovidedAccessTokenException()
        (HttpBridge.instance as ContextualHttpInstance).authorizer.receive(accessToken)
        finish()
    }

    /** Opens the authorization webpage in the browser for the user to enter their credentials. **/
    private fun requestAccessToken() {
        val uri = Uri.parse("${viewModel.url}")
        CustomTabsIntent.Builder().build().launchUrl(this, uri)
    }
}

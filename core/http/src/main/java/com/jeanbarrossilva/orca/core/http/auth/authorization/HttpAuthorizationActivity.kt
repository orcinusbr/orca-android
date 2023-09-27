package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthorization
import com.jeanbarrossilva.orca.core.http.instance.ContextualHttpInstance
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import io.ktor.http.Url

/**
 * [AppCompatActivity] that visually notifies the user of the background authorization process that
 * takes place when this is created and automatically finishes itself when it's done.
 **/
class HttpAuthorizationActivity internal constructor() :
    ComposableActivity(), OnAccessTokenRequestListener {
    /**
     * [HttpAuthorizationViewModel] from which the [Url] to the authorization webpage will be
     * obtained.
     **/
    private val viewModel by viewModels<HttpAuthorizationViewModel> {
        HttpAuthorizationViewModel.createFactory(application, onAccessTokenRequestListener = this)
    }

    /**
     * [IllegalStateException] thrown if the [HttpAuthorizationActivity] has been started from a
     * deep link and an access token isn't provided.
     **/
    private class UnprovidedAccessTokenException : IllegalStateException()

    @Throws(UnprovidedAccessTokenException::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.data?.run(::sendAccessTokenToAuthorizer)
    }

    @Composable
    override fun Content() {
        HttpAuthorization(viewModel)
    }

    override fun onAccessTokenRequest() {
        val uri = Uri.parse("${viewModel.url}")
        CustomTabsIntent.Builder().build().launchUrl(this, uri)
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
}

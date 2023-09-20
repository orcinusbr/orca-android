package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import io.ktor.http.Url
import kotlin.reflect.KClass
import org.koin.android.ext.android.inject

/**
 * [ComposableActivity] that visually notifies the user of the background authorization process that
 * takes place when this is created and automatically finishes itself when it's done.
 *
 * @param T [HttpAuthorizationViewModel] from which the [Url] to the authorization webpage will be
 * obtained.
 **/
abstract class HttpAuthorizationActivity<T : HttpAuthorizationViewModel> : ComposableActivity() {
    /** [HttpAuthorizer] through which authorization will be performed. **/
    private val authorizer by inject<HttpAuthorizer<*>>()

    /** [KClass] of the [HttpAuthorizationViewModel]. **/
    protected abstract val viewModelClass: KClass<T>

    /** [ViewModelProvider.Factory] that creates the [HttpAuthorizationViewModel]. **/
    protected abstract val viewModelFactory: ViewModelProvider.Factory

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
    final override fun Content() {
        HttpAuthorization()
    }

    /**
     * Gets the access token from the [deepLink].
     *
     * @param deepLink [Uri] from which this [HttpAuthorizationActivity] was started after
     * requesting for the user to be authorized.
     **/
    protected abstract fun getAccessToken(deepLink: Uri): String?

    /**
     * Sends the access token that's been successfully retrieved from the deep link through which
     * this [HttpAuthorizationActivity] was started to the [authorizer] or requests the access token
     * if it's been started directly by Orca.
     *
     * @throws UnprovidedAccessTokenException If this [HttpAuthorizationActivity] has been started
     * from a deep link and an access token hasn't been provided.
     **/
    @Throws(UnprovidedAccessTokenException::class)
    private fun requestAccessTokenOrSendToAuthorizer() {
        intent.data?.let(::sendAccessTokenToAuthorizer) ?: requestAccessToken()
    }

    /**
     * Sends the access token that might be in the [deepLink] to the [authorizer].
     *
     * @param deepLink [Uri] from which this [HttpAuthorizationActivity] was started after
     * requesting for the user to be authorized.
     * @throws UnprovidedAccessTokenException If an access token hasn't been provided.
     **/
    @Throws(UnprovidedAccessTokenException::class)
    private fun sendAccessTokenToAuthorizer(deepLink: Uri) {
        val accessToken = getAccessToken(deepLink) ?: throw UnprovidedAccessTokenException()
        authorizer.receive(accessToken)
        finish()
    }

    /** Opens the authorization webpage in the browser for the user to enter their credentials. **/
    private fun requestAccessToken() {
        val viewModel = ViewModelProvider(viewModelStore, viewModelFactory)[viewModelClass.java]
        val uri = Uri.parse("${viewModel.url}")
        CustomTabsIntent.Builder().build().launchUrl(this, uri)
    }
}

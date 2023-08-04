package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on
import org.koin.android.ext.android.inject

internal class AuthorizationActivity : ComposableActivity() {
    private val authorizer by inject<MastodonAuthorizer>()
    private val viewModel by viewModels<AuthorizationViewModel> {
        AuthorizationViewModel.createFactory(application)
    }

    class UnavailableAccessTokenException internal constructor() : IllegalStateException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestAccessTokenOrSendToAuthorizer()
    }

    @Composable
    override fun Content() {
        Authorization()
    }

    private fun requestAccessTokenOrSendToAuthorizer() {
        intent.data?.let(::sendAccessTokenToAuthorizer) ?: requestAccessToken()
    }

    private fun sendAccessTokenToAuthorizer(uri: Uri) {
        val accessToken = uri.getQueryParameter("code") ?: throw UnavailableAccessTokenException()
        authorizer.receive(accessToken)
        finish()
    }

    private fun requestAccessToken() {
        val uri = Uri.parse("${viewModel.url}")
        CustomTabsIntent.Builder().build().launchUrl(this, uri)
    }

    companion object {
        fun start(context: Context) {
            context.on<AuthorizationActivity>().asNewTask().start()
        }
    }
}

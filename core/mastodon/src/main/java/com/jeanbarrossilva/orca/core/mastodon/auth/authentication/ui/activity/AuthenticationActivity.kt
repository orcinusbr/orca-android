package com.jeanbarrossilva.orca.core.mastodon.auth.authentication.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.ui.Authentication
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.ui.AuthenticationViewModel
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import org.koin.android.ext.android.inject

internal class AuthenticationActivity : ComposableActivity() {
    private val authenticator by inject<MastodonAuthenticator>()
    private val authorizationCode by argument<String>(AUTHORIZATION_CODE_KEY)
    private val viewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModel.createFactory(application, authorizationCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request()
    }

    @Composable
    override fun Content() {
        Authentication()
    }

    private fun request() {
        viewModel.withActor {
            authenticator.receive(it)
            finish()
        }
    }

    companion object {
        private const val AUTHORIZATION_CODE_KEY = "authorization-code"

        fun start(context: Context, authorizationCode: String) {
            val intent = Intent<AuthenticationActivity>(
                context,
                AUTHORIZATION_CODE_KEY to authorizationCode
            )
                .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
            context.startActivity(intent)
        }
    }
}

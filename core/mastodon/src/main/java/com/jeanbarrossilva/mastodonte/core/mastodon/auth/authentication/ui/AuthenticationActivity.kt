package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.mastodonte.platform.ui.core.argument
import com.jeanbarrossilva.mastodonte.platform.ui.core.composable.ComposableActivity
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
            val extras = bundleOf(AUTHORIZATION_CODE_KEY to authorizationCode)
            val intent = Intent(context, AuthenticationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtras(extras)
            }
            context.startActivity(intent)
        }
    }
}

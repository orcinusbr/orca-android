package com.jeanbarrossilva.orca.feature.auth

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on
import org.koin.android.ext.android.inject

class AuthActivity internal constructor() : ComposableActivity() {
    private val authenticator by inject<Authenticator>()
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModel.createFactory(authenticator, listener = ::finish)
    }

    @Composable
    override fun Content() {
        Auth(viewModel)
    }

    companion object {
        fun start(context: Context) {
            context.on<AuthActivity>().start()
        }
    }
}

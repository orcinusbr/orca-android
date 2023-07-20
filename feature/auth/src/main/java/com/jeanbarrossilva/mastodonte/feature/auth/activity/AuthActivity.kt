package com.jeanbarrossilva.mastodonte.feature.auth.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.feature.auth.Auth
import com.jeanbarrossilva.mastodonte.feature.auth.AuthViewModel
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.CompleteLifecycleActivity
import org.koin.android.ext.android.inject

class AuthActivity internal constructor() : CompleteLifecycleActivity() {
    private val authorizer by inject<Authorizer>()
    private val authenticator by inject<Authenticator>()
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModel.createFactory(authorizer, authenticator, listener = ::finish)
    }

    @Composable
    override fun Content() {
        Auth(viewModel)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }
}

package com.jeanbarrossilva.mastodonte.feature.auth.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.feature.auth.Auth
import com.jeanbarrossilva.mastodonte.feature.auth.AuthViewModel
import com.jeanbarrossilva.mastodonte.platform.ui.core.composable.ComposableActivity
import org.koin.android.ext.android.inject

class AuthActivity internal constructor() : ComposableActivity() {
    private val authorizer by inject<Authorizer>()
    private val authenticator by inject<Authenticator>()
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModel.createFactory(authorizer, authenticator)
    }

    internal var lifecycleState: LifecycleState? = null

    internal enum class LifecycleState {
        CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        DESTROYED
    }

    @Composable
    override fun Content() {
        Auth(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleState = LifecycleState.CREATED
    }

    override fun onStart() {
        super.onStart()
        lifecycleState = LifecycleState.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleState = LifecycleState.RESUMED
    }

    override fun onPause() {
        super.onPause()
        lifecycleState = LifecycleState.PAUSED
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleState = LifecycleState.DESTROYED
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }
}

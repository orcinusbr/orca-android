package com.jeanbarrossilva.orca.feature.auth

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on
import org.koin.android.ext.android.get

class AuthActivity internal constructor() : ComposableActivity() {
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModel.createFactory(get<SomeInstance>().authenticator, listener = ::finish)
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

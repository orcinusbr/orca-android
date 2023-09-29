package com.jeanbarrossilva.orca.core.http.auth.authentication.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthentication
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticationViewModel
import com.jeanbarrossilva.orca.core.http.instance.ContextualHttpInstance
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * [ComposableActivity] that visually notifies the user of the background authentication process
 * that takes place when this is created and automatically finishes itself when it's done.
 **/
class HttpAuthenticationActivity : ComposableActivity() {
    /** Code provided by the API when the user was authorized. **/
    private val authorizationCode by extra<String>(AUTHORIZATION_CODE_KEY)

    /**
     * [HttpAuthenticationViewModel] by which an [authenticated][Actor.Authenticated] [Actor] will
     * be requested.
     **/
    private val viewModel by viewModels<HttpAuthenticationViewModel> {
        HttpAuthenticationViewModel.createFactory(application, authorizationCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticate()
    }

    @Composable
    override fun Content() {
        HttpAuthentication()
    }

    /**
     * Requests the user to be authenticated and finishes this [HttpAuthenticationActivity] when
     * it's done.
     **/
    private fun authenticate() {
        viewModel.request {
            (Injector.get<SomeHttpInstance>() as ContextualHttpInstance).authenticator.receive(it)
            finish()
        }
    }

    companion object {
        /** Key for retrieving the authorization code. **/
        private const val AUTHORIZATION_CODE_KEY = "authorization-code"

        /**
         * Starts an [HttpAuthenticationActivity].
         *
         * @param context [Context] in which the [HttpAuthenticationActivity] will be started.
         * @param authorizationCode Code provided by the API when the user was authorized.
         **/
        internal fun start(context: Context, authorizationCode: String) {
            context
                .on<HttpAuthenticationActivity>()
                .asNewTask()
                .with(AUTHORIZATION_CODE_KEY to authorizationCode)
                .start()
        }
    }
}

package com.jeanbarrossilva.orca.core.http.auth.authentication

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.http.auth.authentication.authenticator.HttpAuthenticator
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import kotlin.reflect.KClass
import org.koin.android.ext.android.inject

/**
 * [ComposableActivity] that visually notifies the user of the background authentication process
 * that takes place when this is created and automatically finishes itself when it's done.
 *
 * @param T [HttpAuthenticationViewModel] by which an [Actor] will be requested.
 **/
abstract class HttpAuthenticationActivity<T : HttpAuthenticationViewModel> : ComposableActivity() {
    /** [HttpAuthenticator] through which authentication will be performed. **/
    private val authenticator by inject<HttpAuthenticator<*>>()

    /** [KClass] of the [HttpAuthenticationViewModel]. **/
    protected abstract val viewModelClass: KClass<T>

    /** [ViewModelProvider.Factory] that creates the [HttpAuthenticationViewModel]. **/
    protected abstract val viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request()
    }

    @Composable
    override fun Content() {
        HttpAuthentication()
    }

    /**
     * Requests authorization for the user and finishes this [HttpAuthenticationActivity] when it's
     * done.
     **/
    private fun request() {
        ViewModelProvider(viewModelStore, viewModelFactory)[viewModelClass.java].withActor {
            authenticator.receive(it)
            finish()
        }
    }
}

package com.jeanbarrossilva.mastodonte.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AuthViewModel private constructor(
    private val authorizer: Authorizer,
    private val authenticator: Authenticator,
    private val listener: OnAuthenticationListener
) : ViewModel() {
    private val usernameMutableFlow = MutableStateFlow("")
    private val instanceMutableFlow = MutableStateFlow("")

    val usernameFlow = usernameMutableFlow.asStateFlow()
    val instanceFlow = instanceMutableFlow.asStateFlow()

    fun setUsername(username: String) {
        usernameMutableFlow.value = username
    }

    fun setInstance(instance: String) {
        instanceMutableFlow.value = instance
    }

    fun signIn() {
        viewModelScope.launch {
            authenticator.authenticate(authorizer)
            listener.onAuthentication()
        }
    }

    companion object {
        fun createFactory(
            authorizer: Authorizer,
            authenticator: Authenticator,
            listener: OnAuthenticationListener
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AuthViewModel(authorizer, authenticator, listener)
                }
            }
        }
    }
}

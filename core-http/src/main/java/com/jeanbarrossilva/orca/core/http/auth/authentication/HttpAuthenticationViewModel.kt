package com.jeanbarrossilva.orca.core.http.auth.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import kotlinx.coroutines.launch

/**
 * [AndroidViewModel] that requests an [Actor] through [request].
 *
 * @param application [Application] that allows [Context]-specific behavior.
 **/
abstract class HttpAuthenticationViewModel(application: Application) :
    AndroidViewModel(application) {
    internal fun withActor(action: (Actor) -> Unit) {
        viewModelScope.launch {
            action(request())
        }
    }

    /** Requests an [Actor]. **/
    protected abstract suspend fun request(): Actor
}

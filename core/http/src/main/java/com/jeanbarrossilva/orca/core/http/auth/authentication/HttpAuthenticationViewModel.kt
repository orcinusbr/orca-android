package com.jeanbarrossilva.orca.core.http.auth.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.R
import com.jeanbarrossilva.orca.core.http.auth.Mastodon
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.coroutines.launch

/**
 * [AndroidViewModel] that requests an [Actor] through [request].
 *
 * @param application [Application] that allows [Context]-specific behavior.
 * @param authorizationCode Code provided by the API when authorization was granted to the user.
 */
internal class HttpAuthenticationViewModel
private constructor(application: Application, private val authorizationCode: String) :
  AndroidViewModel(application) {
  /**
   * Requests the API to authenticate the user and runs [onAuthentication] with the resulting
   * [Actor].
   *
   * @param onAuthentication Callback run when the user has been successfully authenticated.
   */
  internal fun request(onAuthentication: (Actor.Authenticated) -> Unit) {
    val application = getApplication<Application>()
    val scheme = application.getString(R.string.scheme)
    val redirectUri = application.getString(R.string.redirect_uri, scheme)
    viewModelScope.launch {
      (Injector.from<HttpModule>().instanceProvider().provide() as SomeHttpInstance)
        .client
        .submitForm(
          "/oauth/token",
          Parameters.build {
            set("grant_type", "authorization_code")
            set("code", authorizationCode)
            set("client_id", Mastodon.CLIENT_ID)
            set("client_secret", Mastodon.CLIENT_SECRET)
            set("redirect_uri", redirectUri)
            set("scope", Mastodon.SCOPES)
          }
        )
        .body<HttpAuthenticationToken>()
        .toActor()
        .run(onAuthentication)
    }
  }

  companion object {
    /**
     * Creates a [ViewModelProvider.Factory] that provides an [HttpAuthenticationViewModel].
     *
     * @param application [Application] that allows [Context]-specific behavior.
     * @param authorizationCode Code provided by the API when authorization was granted to the user.
     */
    fun createFactory(
      application: Application,
      authorizationCode: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { HttpAuthenticationViewModel(application, authorizationCode) }
      }
    }
  }
}

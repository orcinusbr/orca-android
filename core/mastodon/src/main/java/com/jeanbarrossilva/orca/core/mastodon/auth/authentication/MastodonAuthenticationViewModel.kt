/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.R
import com.jeanbarrossilva.orca.core.mastodon.auth.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
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
internal class MastodonAuthenticationViewModel
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
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
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
        .body<MastodonAuthenticationToken>()
        .toActor()
        .run(onAuthentication)
    }
  }

  companion object {
    /**
     * Creates a [ViewModelProvider.Factory] that provides a [MastodonAuthenticationViewModel].
     *
     * @param application [Application] that allows [Context]-specific behavior.
     * @param authorizationCode Code provided by the API when authorization was granted to the user.
     */
    fun createFactory(
      application: Application,
      authorizationCode: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { MastodonAuthenticationViewModel(application, authorizationCode) }
      }
    }
  }
}

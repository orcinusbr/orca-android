/*
 * Copyright © 2023–2025 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.auth.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.auth.Mastodon
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.std.func.monad.flatMap
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import io.ktor.client.call.body
import java.net.URI
import kotlinx.coroutines.launch

/**
 * [AndroidViewModel] that requests an [Actor] through [request].
 *
 * @property application [Application] that allows [Context]-specific behavior.
 * @property requester [Requester] with which a [MastodonAuthenticationToken] is converted into an
 *   [Actor].
 * @property avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   the avatar will be loaded from a [URI].
 * @property authorizationCode Code provided by the API when authorization was granted to the user.
 */
internal class MastodonAuthenticationViewModel
private constructor(
  application: Application,
  private val requester: Requester<*>,
  private val avatarLoaderProvider: SomeImageLoaderProvider<URI>,
  private val authorizationCode: String
) : AndroidViewModel(application) {
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
      requester
        .post({ path("oauth").path("token").build() }) {
          parameters {
            append("grant_type", "authorization_code")
            append("code", authorizationCode)
            append("client_id", Mastodon.CLIENT_ID)
            append("client_secret", Mastodon.CLIENT_SECRET)
            append("redirect_uri", redirectUri)
            append("scope", Mastodon.SCOPES)
          }
        }
        .flatMap { it.body<MastodonAuthenticationToken>().toActor(requester, avatarLoaderProvider) }
        .onSuccessful(onAuthentication)
    }
  }

  companion object {
    /**
     * Creates a [ViewModelProvider.Factory] that provides a [MastodonAuthenticationViewModel].
     *
     * @param application [Application] that allows [Context]-specific behavior.
     * @param requester [Requester] with which a [MastodonAuthenticationToken] is converted into an
     *   [Actor].
     * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
     *   the avatar will be loaded from a [URI].
     * @param authorizationCode Code provided by the API when authorization was granted to the user.
     */
    fun createFactory(
      application: Application,
      requester: Requester<*>,
      avatarLoaderProvider: SomeImageLoaderProvider<URI>,
      authorizationCode: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer {
          MastodonAuthenticationViewModel(
            application,
            requester,
            avatarLoaderProvider,
            authorizationCode
          )
        }
      }
    }
  }
}

/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import java.net.URL
import kotlinx.coroutines.launch

/**
 * [AndroidViewModel] that requests an [Actor] through [request].
 *
 * @param application [Application] that allows [Context]-specific behavior.
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   avatar will be loaded from a [URL].
 * @param authorizationCode Code provided by the API when authorization was granted to the user.
 */
internal class MastodonAuthenticationViewModel
private constructor(
  application: Application,
  private val avatarLoaderProvider: SomeImageLoaderProvider<URL>,
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
        .toActor(avatarLoaderProvider)
        .run(onAuthentication)
    }
  }

  companion object {
    /**
     * Creates a [ViewModelProvider.Factory] that provides a [MastodonAuthenticationViewModel].
     *
     * @param application [Application] that allows [Context]-specific behavior.
     * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
     *   the avatar will be loaded from a [URL].
     * @param authorizationCode Code provided by the API when authorization was granted to the user.
     */
    fun createFactory(
      application: Application,
      avatarLoaderProvider: SomeImageLoaderProvider<URL>,
      authorizationCode: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer {
          MastodonAuthenticationViewModel(application, avatarLoaderProvider, authorizationCode)
        }
      }
    }
  }
}

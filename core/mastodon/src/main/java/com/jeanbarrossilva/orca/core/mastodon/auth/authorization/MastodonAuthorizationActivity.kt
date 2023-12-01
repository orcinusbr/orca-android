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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import com.jeanbarrossilva.orca.core.mastodon.instance.ContextualMastodonInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.http.Url

/**
 * [ComposableActivity] that visually notifies the user of the background authorization process that
 * takes place when this is created and automatically finishes itself when it's done.
 */
class MastodonAuthorizationActivity internal constructor() :
  ComposableActivity(), OnAccessTokenRequestListener {
  /**
   * [MastodonAuthorizationViewModel] from which the [Url] to the authorization webpage will be
   * obtained.
   */
  private val viewModel by
    viewModels<MastodonAuthorizationViewModel> {
      MastodonAuthorizationViewModel.createFactory(application, onAccessTokenRequestListener = this)
    }

  /**
   * [IllegalStateException] thrown if the [MastodonAuthorizationActivity] has been started from a
   * deep link and an access token isn't provided.
   */
  private class UnprovidedAccessTokenException : IllegalStateException()

  @Throws(UnprovidedAccessTokenException::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    intent?.data?.run(::sendAccessTokenToAuthorizer)
  }

  @Composable
  override fun Content() {
    MastodonAuthorization(viewModel)
  }

  override fun onAccessTokenRequest() {
    val uri = Uri.parse("${viewModel.url}")
    CustomTabsIntent.Builder().build().launchUrl(this, uri)
  }

  /**
   * Sends the access token that might be in the [deepLink] to the [MastodonAuthorizer].
   *
   * @param deepLink [Uri] from which this [MastodonAuthorizationActivity] was started after
   *   requesting for the user to be authorized.
   * @throws UnprovidedAccessTokenException If an access token hasn't been provided.
   */
  @Throws(UnprovidedAccessTokenException::class)
  private fun sendAccessTokenToAuthorizer(deepLink: Uri) {
    val accessToken = deepLink.getQueryParameter("code") ?: throw UnprovidedAccessTokenException()
    (Injector.from<CoreModule>().instanceProvider().provide() as ContextualMastodonInstance)
      .authorizer
      .receive(accessToken)
    finish()
  }
}

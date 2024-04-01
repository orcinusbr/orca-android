/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.app.module.core

import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import br.com.orcinus.orca.core.mastodon.instance.MastodonInstanceProvider
import br.com.orcinus.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import br.com.orcinus.orca.core.sharedpreferences.feed.profile.post.content.SharedPreferencesTermMuter
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.injectionOf

internal object MainMastodonCoreModule :
  MastodonCoreModule(
    injectionOf {
      MastodonInstanceProvider(
        MainMastodonCoreModule.context,
        MainMastodonCoreModule.authorizer,
        MainMastodonCoreModule.authenticator,
        MainMastodonCoreModule.actorProvider,
        MainMastodonCoreModule.authenticationLock,
        MainMastodonCoreModule.termMuter,
        AsyncImageLoader.Provider
      )
    },
    injectionOf { MainMastodonCoreModule.authenticationLock },
    injectionOf { MainMastodonCoreModule.termMuter }
  ) {
  private val actorProvider by lazy {
    SharedPreferencesActorProvider(context, MainImageLoaderProviderFactory)
  }
  private val authorizer by lazy { MastodonAuthorizer(context) }
  private val authenticator by lazy { MastodonAuthenticator(context, authorizer, actorProvider) }
  private val authenticationLock by lazy { AuthenticationLock(authenticator, actorProvider) }
  private val termMuter by lazy { SharedPreferencesTermMuter(context) }

  private val context
    get() = Injector.get<Context>()
}

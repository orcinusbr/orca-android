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
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.auth.MastodonAuthenticationLock
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import br.com.orcinus.orca.core.mastodon.instance.MastodonInstanceProvider
import br.com.orcinus.orca.core.mastodon.notification.NotificationPermissionLock
import br.com.orcinus.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import br.com.orcinus.orca.core.sharedpreferences.feed.profile.post.content.SharedPreferencesTermMuter
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import br.com.orcinus.orca.std.injector.module.injection.immediateInjectionOf
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

internal fun MastodonCoreModule(
  context: Context,
  notificationPermissionLock: NotificationPermissionLock
): MastodonCoreModule {
  val actorProvider = SharedPreferencesActorProvider(context, MainImageLoaderProviderFactory)
  val authorizer = MastodonAuthorizer(context)
  val authenticator = MastodonAuthenticator(context, authorizer, actorProvider)
  val authenticationLock =
    MastodonAuthenticationLock(context, notificationPermissionLock, authenticator, actorProvider)
  val termMuter = SharedPreferencesTermMuter(context)
  return MastodonCoreModule(
    lazyInjectionOf {
      MastodonInstanceProvider(
        context,
        authorizer,
        authenticator,
        actorProvider,
        authenticationLock,
        termMuter,
        AsyncImageLoader.Provider
      )
    },
    immediateInjectionOf(authenticationLock),
    immediateInjectionOf(termMuter)
  )
}

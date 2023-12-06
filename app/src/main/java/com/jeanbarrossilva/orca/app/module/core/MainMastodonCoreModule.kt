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

package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.mastodon.MastodonCoreModule
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.orca.core.mastodon.instance.MastodonInstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.post.content.SharedPreferencesTermMuter
import com.jeanbarrossilva.orca.std.imageloader.compose.coil.CoilImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector

internal object MainMastodonCoreModule :
  MastodonCoreModule(
    {
      MastodonInstanceProvider(
        context = Injector.get(),
        MainMastodonCoreModule.authorizer,
        MainMastodonCoreModule.authenticator,
        MainMastodonCoreModule.actorProvider,
        MainMastodonCoreModule.authenticationLock,
        MainMastodonCoreModule.termMuter,
        CoilImageLoader.Provider(MainMastodonCoreModule.context)
      )
    },
    { MainMastodonCoreModule.authenticationLock },
    { MainMastodonCoreModule.termMuter }
  ) {
  private val actorProvider by lazy { SharedPreferencesActorProvider(context) }
  private val authorizer by lazy { MastodonAuthorizer(context) }
  private val authenticator by lazy { MastodonAuthenticator(context, authorizer, actorProvider) }
  private val authenticationLock by lazy { AuthenticationLock(authenticator, actorProvider) }
  private val termMuter by lazy { SharedPreferencesTermMuter(context) }

  private val context
    get() = Injector.get<Context>()
}

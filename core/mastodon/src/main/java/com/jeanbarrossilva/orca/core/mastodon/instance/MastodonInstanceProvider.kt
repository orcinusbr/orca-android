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

package com.jeanbarrossilva.orca.core.mastodon.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [InstanceProvider] that provides a [ContextualMastodonInstance].
 *
 * @param context [Context] through which the [Domain] of the [ContextualMastodonInstance] will
 *   retrieved.
 * @param authorizer [MastodonAuthorizer] by which the user will be authorized.
 * @param authenticator [MastodonAuthenticator] for authenticating the user.
 * @param actorProvider [ActorProvider] that provides the [Actor].
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param termMuter [TermMuter] by which [Post]s with muted terms will be filtered out.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image] will be loaded.
 */
class MastodonInstanceProvider(
  private val context: Context,
  private val authorizer: MastodonAuthorizer,
  private val authenticator: MastodonAuthenticator,
  private val actorProvider: ActorProvider,
  private val authenticationLock: AuthenticationLock<MastodonAuthenticator>,
  private val termMuter: TermMuter,
  private val imageLoaderProvider: ImageLoader.Provider<URL>
) : InstanceProvider {
  override fun provide(): SomeInstance {
    return ContextualMastodonInstance(
      context,
      MastodonAuthorizationViewModel.getInstanceDomain(context),
      authorizer,
      authenticator,
      actorProvider,
      authenticationLock,
      termMuter,
      imageLoaderProvider
    )
  }
}

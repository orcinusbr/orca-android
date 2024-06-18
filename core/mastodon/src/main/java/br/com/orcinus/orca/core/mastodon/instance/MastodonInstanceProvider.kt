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

package br.com.orcinus.orca.core.mastodon.instance

import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import br.com.orcinus.orca.core.instance.InstanceProvider
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import br.com.orcinus.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

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
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which image
 *   will be loaded.
 */
class MastodonInstanceProvider(
  private val context: Context,
  private val authorizer: MastodonAuthorizer,
  private val authenticator: MastodonAuthenticator,
  private val actorProvider: ActorProvider,
  private val authenticationLock: AuthenticationLock<MastodonAuthenticator>,
  private val termMuter: TermMuter,
  private val imageLoaderProvider: SomeImageLoaderProvider<URI>
) : InstanceProvider {
  /** [MastodonInstance] to be provided. */
  private val instance by lazy {
    ContextualMastodonInstance(
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

  override fun provide(): SomeMastodonInstance {
    return instance
  }
}

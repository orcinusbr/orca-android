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

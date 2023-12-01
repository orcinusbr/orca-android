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

package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.mastodon.MastodonCoreModule
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.orca.core.mastodon.instance.MastodonInstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.post.content.SharedPreferencesTermMuter
import com.jeanbarrossilva.orca.std.imageloader.compose.CoilImageLoader
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

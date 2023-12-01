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

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthentication
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticationViewModel
import com.jeanbarrossilva.orca.core.mastodon.instance.ContextualMastodonInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * [ComposableActivity] that visually notifies the user of the background authentication process
 * that takes place when this is created and automatically finishes itself when it's done.
 */
class MastodonAuthenticationActivity : ComposableActivity() {
  /** [CoreModule] into which core-HTTP-related dependencies have been injected. */
  private val module by lazy { Injector.from<CoreModule>() }

  /** Code provided by the API when the user was authorized. */
  private val authorizationCode by extra<String>(AUTHORIZATION_CODE_KEY)

  /**
   * [MastodonAuthenticationViewModel] by which an [authenticated][Actor.Authenticated] [Actor] will
   * be requested.
   */
  private val viewModel by
    viewModels<MastodonAuthenticationViewModel> {
      MastodonAuthenticationViewModel.createFactory(application, authorizationCode)
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    authenticate()
  }

  @Composable
  override fun Content() {
    MastodonAuthentication()
  }

  /**
   * Requests the user to be authenticated and finishes this [MastodonAuthenticationActivity] when
   * it's done.
   */
  private fun authenticate() {
    viewModel.request {
      (module.instanceProvider().provide() as ContextualMastodonInstance).authenticator.receive(it)
      finish()
    }
  }

  companion object {
    /** Key for retrieving the authorization code. */
    private const val AUTHORIZATION_CODE_KEY = "authorization-code"

    /**
     * Starts a [MastodonAuthenticationActivity].
     *
     * @param context [Context] in which the [MastodonAuthenticationActivity] will be started.
     * @param authorizationCode Code provided by the API when the user was authorized.
     */
    internal fun start(context: Context, authorizationCode: String) {
      context
        .on<MastodonAuthenticationActivity>()
        .asNewTask()
        .with(AUTHORIZATION_CODE_KEY to authorizationCode)
        .start()
    }
  }
}

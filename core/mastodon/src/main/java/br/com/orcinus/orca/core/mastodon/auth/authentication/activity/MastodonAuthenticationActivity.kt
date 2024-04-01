/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.auth.authentication.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import br.com.orcinus.orca.composite.composable.ComposableActivity
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.instance.InstanceProvider
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthentication
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticationViewModel
import br.com.orcinus.orca.core.mastodon.instance.ContextualMastodonInstance
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.starter.on
import br.com.orcinus.orca.std.injector.Injector

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
      MastodonAuthenticationViewModel.createFactory(
        application,
        instance.imageLoaderProvider,
        authorizationCode
      )
    }

  /**
   * [ContextualMastodonInstance] provided by the [module]'s [InstanceProvider].
   *
   * @see CoreModule.instanceProvider
   */
  private val instance
    get() = module.instanceProvider().provide() as ContextualMastodonInstance

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
      instance.authenticator.receive(it)
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

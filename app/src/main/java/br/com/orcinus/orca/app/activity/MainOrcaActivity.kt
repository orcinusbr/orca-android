/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.app.activity

import br.com.orcinus.orca.app.module.core.MastodonCoreModule
import br.com.orcinus.orca.app.module.feature.profiledetails.MainProfileDetailsModule
import br.com.orcinus.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.notification.NotificationLock
import br.com.orcinus.orca.std.injector.Injector

/**
 * [OrcaActivity] for the main version of Orca, whose [coreModule] contains core structures which
 * perform HTTP requests to the Mastodon API through the [Requester] that gets injected when it is
 * created.
 */
internal class MainOrcaActivity : OrcaActivity() {
  /** [NotificationLock] for unlocking notification sending. */
  private val notificationLock = NotificationLock(this)

  override val coreModule by lazy { MastodonCoreModule(this, notificationLock) }
  override val profileDetailsModule by lazy { MainProfileDetailsModule(this) }

  override fun inject() {
    super.inject()
    Injector.injectLazily {
      Requester.create(MastodonAuthorizationViewModel.getDomain(this@MainOrcaActivity).uri)
    }
  }
}

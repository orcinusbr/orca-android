/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.auth

import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.notification.NotificationLock
import br.com.orcinus.orca.platform.autos.i18n.ReadableThrowable

/**
 * [AuthenticationLock] whose [authenticator] authenticates via the Mastodon API.
 *
 * @property context [Context] by which default and localized messages for [Exception]s thrown when
 *   authentication fails to be performed are provided.
 * @property notificationLock [NotificationLock] by which the permission to send notifications is
 *   unlocked.
 */
class MastodonAuthenticationLock(
  private val context: Context,
  private val notificationLock: NotificationLock,
  override val authorizer: Authorizer,
  override val authenticator: MastodonAuthenticator,
  override val actorProvider: ActorProvider
) : AuthenticationLock<MastodonAuthenticator>() {
  /** Whether an unlock has not been performed before. */
  private var isOnFirstUnlock = true

  override suspend fun onUnlock(actor: Actor.Authenticated) {
    if (isOnFirstUnlock) {
      notificationLock.requestUnlock()
      isOnFirstUnlock = false
    }
  }

  override fun createFailedAuthenticationException(): FailedAuthenticationException {
    return FailedAuthenticationException(
      cause = ReadableThrowable(context, R.string.core_mastodon_failed_authentication_error)
    )
  }
}

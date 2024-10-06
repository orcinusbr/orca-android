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

package br.com.orcinus.orca.core.mastodon.notification

import android.app.Notification
import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import kotlinx.coroutines.future.await

/**
 * Converts this [MastodonNotification] into a [Notification].
 *
 * @param context [Context] with which the underlying [Notification.Builder] will be instantiated.
 * @param authenticationLock [AuthenticationLock] for checking whether the [MastodonAccount] is
 *   owned by the current [Actor].
 * @see MastodonNotification.account
 * @see MastodonAccount.isOwned
 */
internal suspend fun MastodonNotification.toNotification(
  context: Context,
  authenticationLock: SomeAuthenticationLock
): Notification {
  return toNotificationAsync(context, authenticationLock).await()
}

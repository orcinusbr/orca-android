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

package br.com.orcinus.orca.core.mastodon.feed.profile.account

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the relationship between two [MastodonAccount]s is requested.
 *
 * @property following Whether the currently [authenticated][Actor.Authenticated] [Actor] is
 *   following the other [MastodonAccount] to which this refers to.
 */
@Serializable
internal data class MastodonRelationship(private val following: Boolean) {
  /**
   * Converts this [MastodonRelationship] into a follow status.
   *
   * @param isAccountLocked Whether the [MastodonAccount] of the user that has a relationship with
   *   the currently [authenticated][Actor.Authenticated] [Actor] is locked.
   */
  fun toFollow(isAccountLocked: Boolean): Follow {
    return when {
      isAccountLocked && following -> Follow.Private.following()
      isAccountLocked -> Follow.Private.unfollowed()
      following -> Follow.Public.following()
      else -> Follow.Public.unfollowed()
    }
  }
}

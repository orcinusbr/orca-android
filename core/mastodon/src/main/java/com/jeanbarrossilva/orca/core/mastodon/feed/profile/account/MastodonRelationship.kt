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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.account

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the relationship between two [MastodonAccount]s is requested.
 *
 * @param following Whether the currently [authenticated][Actor.Authenticated] [Actor] is following
 *   the other [MastodonAccount] to which this refers to.
 */
@Serializable
internal data class MastodonRelationship(val following: Boolean) {
  /**
   * Converts this [MastodonRelationship] into a [MastodonAccount].
   *
   * @param account [MastodonAccount] of the user that has a relationship with the currently
   *   [authenticated][Actor.Authenticated] [Actor].
   */
  fun toFollow(account: MastodonAccount): Follow {
    return when {
      account.locked && following -> Follow.Private.following()
      account.locked -> Follow.Private.unfollowed()
      following -> Follow.Public.following()
      else -> Follow.Public.unfollowed()
    }
  }
}

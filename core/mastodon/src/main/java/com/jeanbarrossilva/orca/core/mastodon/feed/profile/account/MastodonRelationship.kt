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

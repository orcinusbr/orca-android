package com.jeanbarrossilva.orca.core.http.feed.profile.account

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the relationship between two [HttpAccount]s is requested.
 *
 * @param following Whether the currently [authenticated][Actor.Authenticated] [Actor] is following
 *   the other [HttpAccount] to which this refers to.
 */
@Serializable
internal data class HttpRelationship(val following: Boolean) {
  /**
   * Converts this [HttpRelationship] into an [HttpAccount].
   *
   * @param account [HttpAccount] of the user that has a relationship with the currently
   *   [authenticated][Actor.Authenticated] [Actor].
   */
  fun toFollow(account: HttpAccount): Follow {
    return when {
      account.locked && following -> Follow.Private.following()
      account.locked -> Follow.Private.unfollowed()
      following -> Follow.Public.following()
      else -> Follow.Public.unfollowed()
    }
  }
}

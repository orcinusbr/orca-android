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

package br.com.orcinus.orca.core.mastodon.feed.profile.type.followable

import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowService
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfileProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated

/**
 * [FollowService] which communicates with the Mastodon API in order to toggle a [Follow] status.
 *
 * @property requester [Requester] by which the HTTP requests are sent.
 */
class MastodonFollowService(
  private val requester: Requester,
  override val profileProvider: MastodonProfileProvider
) : FollowService() {
  override suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T) {
    requester
      .authenticated()
      .post({
        path("api")
          .path("v1")
          .path("accounts")
          .path(profile.id)
          .run {
            when (follow) {
              is Follow.Public.Following,
              is Follow.Public.Subscribed,
              is Follow.Private.Following,
              is Follow.Private.Subscribed -> path("follow")
              else -> path("unfollow")
            }
          }
          .build()
      })
  }
}

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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.stat

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated

/**
 * Builds a [ToggleableStat] for a [MastodonPost]'s favorites that obtains them from the API.
 *
 * @param requester [Requester] by which a request to toggle the "favorited" status is performed.
 * @param id ID of the [MastodonPost] for which the [ToggleableStat] is.
 * @param count Amount of times that the [MastodonPost] has been marked as favorite.
 */
@Suppress("FunctionName")
internal fun FavoriteStat(requester: Requester, id: String, count: Int): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    onSetEnabled { isEnabled ->
      requester
        .authenticated()
        .post({
          path("api")
            .path("v1")
            .path("statuses")
            .path(id)
            .run { if (isEnabled) path("favourite") else path("unfavourite") }
            .build()
        })
    }
  }
}

/**
 * Builds a [ToggleableStat] for a [MastodonPost]'s reposts that obtains them from the API.
 *
 * @param requester [Requester] by which a request to toggle the "reposted" status is performed.
 * @param id ID of the [MastodonPost] for which the [ToggleableStat] is.
 * @param count Amount of times that the [MastodonPost] has been reposted.
 */
@Suppress("FunctionName")
internal fun RepostStat(requester: Requester, id: String, count: Int): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    onSetEnabled { isEnabled ->
      requester
        .authenticated()
        .post({
          path("api")
            .path("v1")
            .path("statuses")
            .path(id)
            .run { if (isEnabled) path("reblog") else path("unreblog") }
            .build()
        })
    }
  }
}

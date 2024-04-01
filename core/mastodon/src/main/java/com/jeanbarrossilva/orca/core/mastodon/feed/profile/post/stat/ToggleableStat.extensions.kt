/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndPost
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeMastodonInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * Builds a [ToggleableStat] for A [MastodonPost]'s favorites that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [ToggleableStat] is.
 * @param count Amount of times that the [MastodonPost] has been marked as favorite.
 */
@Suppress("FunctionName")
internal fun FavoriteStat(id: String, count: Int): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    onSetEnabled { isEnabled ->
      val route =
        if (isEnabled) {
          "/api/v1/statuses/$id/favourite"
        } else {
          @Suppress("SpellCheckingInspection") "/api/v1/statuses/$id/unfavourite"
        }
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .client
        .authenticateAndPost(route)
    }
  }
}

/**
 * Builds a [ToggleableStat] for A [MastodonPost]'s reblogs that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [ToggleableStat] is.
 * @param count Amount of times that the [MastodonPost] has been reblogged.
 */
@Suppress("FunctionName")
internal fun ReblogStat(id: String, count: Int): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    onSetEnabled { isEnabled ->
      val route = if (isEnabled) "/api/v1/statuses/$id/reblog" else "/api/v1/statuses/$id/unreblog"
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .client
        .authenticateAndPost(route)
    }
  }
}

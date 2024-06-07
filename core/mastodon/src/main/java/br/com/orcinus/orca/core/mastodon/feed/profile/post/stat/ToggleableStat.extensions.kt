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
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.injector.Injector

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
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .requester
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
 * Builds a [ToggleableStat] for A [MastodonPost]'s reblogs that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [ToggleableStat] is.
 * @param count Amount of times that the [MastodonPost] has been reblogged.
 */
@Suppress("FunctionName")
internal fun ReblogStat(id: String, count: Int): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    onSetEnabled { isEnabled ->
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .requester
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

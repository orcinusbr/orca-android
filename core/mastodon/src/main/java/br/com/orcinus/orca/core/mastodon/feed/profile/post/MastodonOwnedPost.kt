/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post

import androidx.annotation.CheckResult
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated

/**
 * [OwnedPost] that is removed by sending a request to the Mastodon API.
 *
 * @property delegate [MastodonPost] to delegate its functionality to.
 */
data class MastodonOwnedPost internal constructor(private val delegate: MastodonPost) :
  OwnedPost(delegate) {
  @CheckResult
  override suspend fun remove() =
    delegate.requester
      .authenticated()
      .delete({ path("api").path("v1").path("statuses").path(id).build() })
      .unit()
}

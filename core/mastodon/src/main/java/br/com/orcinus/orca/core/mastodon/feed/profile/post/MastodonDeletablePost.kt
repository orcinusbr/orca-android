/*
 * Copyright © 2023-2024 Orcinus
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

import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.mastodon.instanceProvider
import br.com.orcinus.orca.core.mastodon.network.client.authenticateAndDelete
import br.com.orcinus.orca.std.injector.Injector

/**
 * [DeletablePost] that is deleted by sending a request to the Mastodon API.
 *
 * @param delegate [MastodonPost] to delegate its functionality to.
 */
internal data class MastodonDeletablePost(private val delegate: MastodonPost) :
  DeletablePost(delegate) {
  override suspend fun delete() {
    (Injector.from<MastodonCoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .client
      .authenticateAndDelete("api/v1/statuses/$id")
  }
}

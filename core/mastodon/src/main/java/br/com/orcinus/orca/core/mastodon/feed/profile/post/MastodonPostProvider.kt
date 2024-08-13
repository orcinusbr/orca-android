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

package br.com.orcinus.orca.core.mastodon.feed.profile.post

import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.platform.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * [PostProvider] that either requests [Post]s to the API or retrieves cached ones if they're
 * available.
 *
 * @param cache [Cache] of [Post]s by which [Post]s will be obtained.
 */
class MastodonPostProvider
internal constructor(
  override val authenticationLock: SomeAuthenticationLock,
  private val cache: Cache<Post>
) : PostProvider() {
  override suspend fun onProvide(id: String): Flow<Post> {
    val post = cache.get(id)
    return flowOf(post)
  }
}

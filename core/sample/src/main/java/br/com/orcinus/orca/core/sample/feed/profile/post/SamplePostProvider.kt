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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.provider.PostProvider
import br.com.orcinus.orca.core.sample.auth.sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * [PostProvider] that provides sample [Posts].
 *
 * @param defaultPosts [Post]s that are present by default.
 */
class SamplePostProvider internal constructor(internal val defaultPosts: Posts = Posts()) :
  PostProvider() {
  /** [MutableStateFlow] that provides the [Post]s. */
  internal val postsFlow = MutableStateFlow(defaultPosts)

  override val authenticationLock = AuthenticationLock.sample

  override suspend fun onProvide(id: String): Flow<Post> {
    return provideAll().mapNotNull { posts -> posts.find { post -> post.id == id } }
  }

  /** Provides all published [Post]s. */
  fun provideAll(): Flow<List<Post>> {
    return postsFlow.asStateFlow()
  }

  /**
   * Provides the [Post]s made by the author whose ID equals to the given one.
   *
   * @param authorID ID of the author whose [Post]s will be provided.
   */
  fun provideBy(authorID: String): Flow<List<Post>> {
    return provideAll().map { posts -> posts.filter { post -> post.author.id == authorID } }
  }
}

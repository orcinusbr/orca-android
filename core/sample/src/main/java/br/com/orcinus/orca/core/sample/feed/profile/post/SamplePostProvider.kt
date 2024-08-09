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

import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.provider.PostProvider
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.sample.auth.actor.sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking

/** [PostProvider] that provides sample [Post]s. */
class SamplePostProvider(override val authenticationLock: SomeAuthenticationLock) : PostProvider() {
  /** [MutableStateFlow] that provides the [Post]s. */
  internal val postsFlow = MutableStateFlow(emptyList<Post>())

  override suspend fun onProvide(id: String): Flow<Post> {
    return postsFlow.mapNotNull { posts -> posts.find { post -> post.id == id } }
  }

  /** Provides one [Post] originally published by the authenticated [Actor]. */
  fun provideOneCurrent(): Post {
    return provideAllCurrentBy(Actor.Authenticated.sample.id).filterNot { it is Repost }.first()
  }

  /**
   * Adds various [Post]s.
   *
   * @param posts [Post]s to be added.
   * @throws IllegalArgumentException If a [Post] with the same ID as that of one that is already
   *   present is added.
   */
  @Throws(IllegalArgumentException::class)
  fun add(vararg posts: Post) {
    posts.forEach {
      val isUnique = it.id !in provideAllCurrent().map(Post::id)
      require(isUnique) { "A post with the same ID (${it.id}) already exists." }
      postsFlow.value += it
    }
  }

  /** Provides all current [Post]s. */
  fun provideAllCurrent(): List<Post> {
    return postsFlow.value
  }

  /**
   * Provides the current [Post]s made by the author whose ID equals to the given one.
   *
   * @param authorID ID of the author whose [Post]s will be provided.
   */
  fun provideAllCurrentBy(authorID: String): List<Post> {
    /*
     * SamplePostProvider's provideBy(String): Flow<List<Post>>'s flow emits immediately. The
     * execution flow is not blocked.
     */
    return runBlocking { provideAllBy(authorID).first() }
  }

  /**
   * Deletes the [Post] identified by the [id].
   *
   * @param id ID of the [Post] to be deleted.
   * @see Post.id
   */
  fun delete(id: String) {
    postsFlow.value -= provideAllCurrent().single { post -> post.id == id }
  }

  /**
   * Provides the [Post]s made by the author whose ID equals to the given one.
   *
   * @param authorID ID of the author whose [Post]s will be provided.
   */
  internal fun provideAllBy(authorID: String): Flow<List<Post>> {
    return postsFlow.map { posts -> posts.filter { post -> post.author.id == authorID } }
  }
}

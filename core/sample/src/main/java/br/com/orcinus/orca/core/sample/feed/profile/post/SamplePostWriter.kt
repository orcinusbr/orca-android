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

import br.com.orcinus.orca.core.feed.profile.post.Post
import kotlinx.coroutines.flow.update

/**
 * Performs [SamplePost]-related writing operations.
 *
 * @param postProvider [SamplePostProvider] by which [SamplePost]s will be provided.
 */
class SamplePostWriter
internal constructor(internal val postProvider: SamplePostProvider = SamplePostProvider()) {
  /** Provides a [SamplePostWriter] through [provide]. */
  class Provider internal constructor() {
    /** [SamplePostWriter] to be provided. */
    private var writer: SamplePostWriter? = null

    /**
     * [IllegalStateException] to be thrown if a [SamplePostWriter] is requested to be provided but
     * none has been specified.
     */
    internal class UnspecifiedWriterException :
      IllegalStateException("A post writer to be provided hasn't been specified.")

    /**
     * Provides the specified [SamplePostWriter].
     *
     * @throws UnspecifiedWriterException If a [SamplePostWriter] to be provided hasn't been
     *   specified.
     */
    @Throws(UnspecifiedWriterException::class)
    fun provide(): SamplePostWriter {
      return writer ?: throw UnspecifiedWriterException()
    }

    /** Defines the given [SamplePostWriter] as the one to be provided. */
    internal fun provide(writer: SamplePostWriter) {
      this.writer = writer
    }
  }

  /**
   * Adds the [post].
   *
   * @param post [Post] to be added.
   * @throws IllegalArgumentException If a [Post] with the same ID as the given one's is already
   *   present.
   */
  fun add(post: Post) {
    val isUnique = post.id !in postProvider.postsFlow.value.map(Post::id)
    if (isUnique) {
      postProvider.postsFlow.update { it + post }
    } else {
      throw IllegalArgumentException("A post with the same ID (${post.id}) already exists.")
    }
  }

  /**
   * Deletes the [SamplePost] identified by the [id].
   *
   * @param id ID of the [SamplePost] to be deleted.
   * @see SamplePost.id
   */
  fun delete(id: String) {
    postProvider.postsFlow.update { posts -> posts - posts.single { post -> post.id == id } }
  }

  /** Resets this [SamplePostWriter] to its default state. */
  fun reset() {
    postProvider.postsFlow.update { postProvider.defaultPosts }
  }
}

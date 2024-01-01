/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post

/**
 * [List] of [Post]s that have been added from a [Builder.AdditionScope].
 *
 * @param additionScope [Builder.AdditionScope] in which the [Post]s were added.
 * @param delegate [List] to which the functionality will be delegated.
 */
class Posts
private constructor(
  internal val additionScope: Builder.AdditionScope,
  private val delegate: List<Post>
) : List<Post> by delegate {
  /**
   * Configures and builds [Posts].
   *
   * @see build
   */
  class Builder internal constructor() {
    /** [AdditionScope] in which this [Builder] will add [Post]s. */
    private val additionScope = AdditionScope()

    /** [List] on top of which [Posts] will be created. */
    private val delegate = mutableListOf<Post>()

    /** Scope in which a [Post] is added. */
    class AdditionScope internal constructor() {
      /**
       * [SamplePostWriter.Provider] that provides the [SamplePostWriter] for the [Post] to perform
       * its write operations.
       */
      internal val writerProvider = SamplePostWriter.Provider()

      /**
       * Marks the [Post] addition process as finished, specifying a [SamplePostWriter] with the
       * given [posts] to be provided by the [writerProvider].
       *
       * @param posts [Posts] with which the [SamplePostWriter] will be provided.
       */
      internal fun finish(posts: Posts) {
        val postProvider = SamplePostProvider(posts)
        val writer = SamplePostWriter(postProvider)
        writerProvider.provide(writer)
      }
    }

    /**
     * Adds multiple [Post]s.
     *
     * @param addition Returns the [Post]s to be added within the [additionScope].
     */
    fun addAll(addition: AdditionScope.() -> Collection<Post>): Builder {
      val posts = additionScope.addition()
      delegate.addAll(posts)
      return this
    }

    /**
     * Adds a [Post].
     *
     * @param addition Returns the [Post] to be added within the [additionScope].
     */
    fun add(addition: AdditionScope.() -> Post): Builder {
      val post = additionScope.addition()
      delegate.add(post)
      return this
    }

    /** Builds [Posts]. */
    internal fun build(): Posts {
      val delegateAsList = delegate.toList()
      return Posts(additionScope, delegateAsList).also(additionScope::finish)
    }
  }

  /**
   * Creates [Posts] with the given [Post] appended to it.
   *
   * @param other [Post] to be added.
   */
  internal operator fun plus(other: Post): Posts {
    return Posts { addAll { delegate + other } }
  }

  /**
   * Creates [Posts] without the given [Post].
   *
   * @param other [Post] to be removed.
   */
  internal operator fun minus(other: Post): Posts {
    return Posts { addAll { delegate - other } }
  }

  companion object
}

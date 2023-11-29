package com.jeanbarrossilva.orca.core.feed.profile.post

import kotlinx.coroutines.flow.Flow

/** Provides [Post]s. */
interface PostProvider {
  /**
   * Provides the [Post] identified as [id].
   *
   * @param id ID of the [Post] to be provided.
   * @see Post.id
   */
  suspend fun provide(id: String): Flow<Post>
}

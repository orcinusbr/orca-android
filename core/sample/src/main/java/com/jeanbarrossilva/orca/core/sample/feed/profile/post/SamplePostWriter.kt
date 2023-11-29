package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import kotlinx.coroutines.flow.update

/**
 * Performs [Post]-related writing operations.
 *
 * @param postProvider [SamplePostProvider] by which [Post]s will be provided.
 */
class SamplePostWriter internal constructor(private val postProvider: SamplePostProvider) {
  /** Clears all added [Post]s, including the default ones. */
  fun clear() {
    postProvider.postsFlow.update { emptyList() }
  }

  /** Resets this [SamplePostWriter] to its default state. */
  fun reset() {
    postProvider.postsFlow.value = postProvider.defaultPosts
  }
}

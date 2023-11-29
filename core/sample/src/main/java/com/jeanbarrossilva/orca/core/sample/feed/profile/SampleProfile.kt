package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** [Profile] whose operations are performed in memory and serves as a sample. */
internal interface SampleProfile : Profile {
  /** [SamplePostProvider] by which this [SampleProfile]'s [Post]s will be provided. */
  val postProvider: SamplePostProvider

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    return postProvider.provideBy(id).filterNotNull().map {
      it.windowed(POSTS_PER_PAGE, partialWindows = true).getOrElse(page) { emptyList() }
    }
  }

  companion object {
    /** Maximum amount of [Post]s emitted to [getPosts]. */
    const val POSTS_PER_PAGE = 50
  }
}

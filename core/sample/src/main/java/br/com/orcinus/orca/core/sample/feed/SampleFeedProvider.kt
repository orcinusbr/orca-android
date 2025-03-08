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

package br.com.orcinus.orca.core.sample.feed

import br.com.orcinus.orca.core.feed.FeedProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * [FeedProvider] that provides a feed for a sample [Profile].
 *
 * @property profileProvider [SamplePostProvider] for verifying whether a given user exists.
 * @property postProvider [SamplePostProvider] by which [Post]s will be provided.
 * @property imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   images can be loaded from a [SampleImageSource].
 */
class SampleFeedProvider(
  private val profileProvider: SampleProfileProvider,
  private val postProvider: SamplePostProvider,
  override val termMuter: TermMuter,
  private val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
) : FeedProvider() {
  override suspend fun onProvide(page: Int) =
    postProvider.postsFlow.map {
      it.chunked(Composer.MAX_POST_COUNT_PER_PAGE).getOrElse(page) { emptyList() }
    }

  /**
   * Provides the feed as it currently is.
   *
   * @param page Index of the [Post]s that compose the feed.
   */
  fun provideCurrent(page: Int) =
    /*
     * SampleFeedProvider's provide(userID, page): Flow<List<Post>>'s flow is composed on top of a
     * state flow; given that the only operation
     */
    runBlocking { provide(page).map { it.first() } }
}

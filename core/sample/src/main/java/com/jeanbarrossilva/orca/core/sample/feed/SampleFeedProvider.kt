/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.core.sample.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * [FeedProvider] that provides a feed for a sample [Profile].
 *
 * @param profileAvatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by
 *   which images can be loaded from a [SampleImageSource].
 * @param postProvider [SamplePostProvider] by which [Post]s will be provided.
 */
internal class SampleFeedProvider(
  private val profileAvatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>,
  private val postProvider: SamplePostProvider
) : FeedProvider() {
  override val termMuter = SampleTermMuter()

  /** [Flow] with the posts to be provided in the feed. */
  private val postsFlow = postProvider.postsFlow.asStateFlow()

  override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
    return postsFlow.map { posts ->
      posts.chunked(SampleProfile.POSTS_PER_PAGE).getOrElse(page) { posts.takeLast(1) }
    }
  }

  override suspend fun containsUser(userID: String): Boolean {
    return userID == Profile.createSample(postProvider, profileAvatarLoaderProvider).id
  }
}

package com.jeanbarrossilva.orca.core.sample.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * [FeedProvider] that provides a feed for a sample [Profile].
 *
 * @param profileAvatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by
 *   which [Image]s can be loaded from a [SampleImageSource].
 * @param tootProvider [SampleTootProvider] by which [Toot]s will be provided.
 */
internal class SampleFeedProvider(
  private val profileAvatarLoaderProvider: ImageLoader.Provider<SampleImageSource>,
  private val tootProvider: SampleTootProvider
) : FeedProvider() {
  override val termMuter = SampleTermMuter()

  /** [Flow] with the toots to be provided in the feed. */
  private val tootsFlow = tootProvider.tootsFlow.asStateFlow()

  override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
    return tootsFlow.map {
      it.chunked(SampleProfile.TOOTS_PER_PAGE).getOrElse(page) { emptyList() }
    }
  }

  override suspend fun containsUser(userID: String): Boolean {
    return userID == Profile.createSample(tootProvider, profileAvatarLoaderProvider).id
  }
}

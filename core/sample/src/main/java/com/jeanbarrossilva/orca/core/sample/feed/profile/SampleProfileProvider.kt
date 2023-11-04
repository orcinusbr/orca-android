package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * [ProfileProvider] that provides sample [Profile]s.
 *
 * @param tootProvider [SampleTootProvider] by which [Profile]s' [Toot]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s can be loaded from a [SampleImageSource].
 */
class SampleProfileProvider
internal constructor(
  tootProvider: SampleTootProvider,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
) : ProfileProvider() {
  /** [Profile]s that are present by default. */
  internal val defaultProfiles = listOf(Profile.createSample(tootProvider, imageLoaderProvider))

  /** [MutableStateFlow] that provides the [Profile]s. */
  internal val profilesFlow = MutableStateFlow(defaultProfiles)

  public override suspend fun contains(id: String): Boolean {
    val ids = profilesFlow.value.map(Profile::id)
    return id in ids
  }

  override suspend fun onProvide(id: String): Flow<Profile> {
    return profilesFlow.mapNotNull { profiles -> profiles.find { profile -> profile.id == id } }
  }
}

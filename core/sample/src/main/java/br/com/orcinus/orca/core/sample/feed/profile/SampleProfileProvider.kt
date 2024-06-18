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

package br.com.orcinus.orca.core.sample.feed.profile

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * [ProfileProvider] that provides sample [Profile]s.
 *
 * @param postProvider [SamplePostProvider] by which [Profile]s' [Post]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   can be loaded from a [SampleImageSource].
 */
class SampleProfileProvider
internal constructor(
  postProvider: SamplePostProvider,
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
) : ProfileProvider() {
  /** [Profile]s that are present by default. */
  internal val defaultProfiles = listOf(Profile.createSample(postProvider, imageLoaderProvider))

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

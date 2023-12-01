/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * [ProfileProvider] that provides sample [Profile]s.
 *
 * @param postProvider [SamplePostProvider] by which [Profile]s' [Post]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s can be loaded from a [SampleImageSource].
 */
class SampleProfileProvider
internal constructor(
  postProvider: SamplePostProvider,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
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

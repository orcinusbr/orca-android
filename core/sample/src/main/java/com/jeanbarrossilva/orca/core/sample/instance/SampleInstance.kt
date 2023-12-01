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

package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [Instance] made out of sample underlying core structures.
 *
 * @param defaultPosts [Post]s that are provided by default by the [postProvider].
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
class SampleInstance
internal constructor(
  defaultPosts: List<Post>,
  internal val imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
) : Instance<Authenticator>() {
  override val domain = Domain.sample
  override val authenticator: Authenticator = SampleAuthenticator
  override val authenticationLock = AuthenticationLock(authenticator, ActorProvider.sample)
  override val postProvider = SamplePostProvider(defaultPosts)
  override val feedProvider: FeedProvider = SampleFeedProvider(imageLoaderProvider, postProvider)
  override val profileProvider: ProfileProvider =
    SampleProfileProvider(postProvider, imageLoaderProvider)
  override val profileSearcher: ProfileSearcher =
    SampleProfileSearcher(profileProvider as SampleProfileProvider)

  /** [SampleProfileWriter] for performing write operations on the [profileProvider]. */
  val profileWriter = SampleProfileWriter(profileProvider as SampleProfileProvider)

  /** [SamplePostWriter] for performing write operations on the [postProvider]. */
  val postWriter = SamplePostWriter(postProvider)
}

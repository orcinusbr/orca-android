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

package br.com.orcinus.orca.core.sample.instance

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.feed.FeedProvider
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearcher
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.sample
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileWriter
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostWriter
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider

/**
 * [Instance] made out of sample underlying core structures.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 * @param defaultPosts [Post]s that are provided by default by the [postProvider].
 */
class SampleInstance
internal constructor(
  internal val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>,
  private val defaultPosts: Posts
) : Instance<Authenticator>() {
  override val domain = Domain.sample
  override val authenticator: Authenticator = SampleAuthenticator
  override val authenticationLock = AuthenticationLock.sample
  override val postProvider = postWriter.postProvider
  override val feedProvider: FeedProvider = SampleFeedProvider(imageLoaderProvider, postProvider)
  override val profileProvider: ProfileProvider =
    SampleProfileProvider(postProvider, imageLoaderProvider)
  override val profileSearcher: ProfileSearcher =
    SampleProfileSearcher(profileProvider as SampleProfileProvider)

  /** [SampleProfileWriter] for performing write operations on the [profileProvider]. */
  val profileWriter = SampleProfileWriter(profileProvider as SampleProfileProvider)

  /** [SamplePostWriter] for performing write operations on the [postProvider]. */
  val postWriter
    get() = defaultPosts.additionScope.writerProvider.provide()
}

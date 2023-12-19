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
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider

/**
 * [Instance] made out of sample underlying core structures.
 *
 * @param defaultPosts [Post]s that are provided by default by the [postProvider].
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
class SampleInstance
internal constructor(
  defaultPosts: List<Post>,
  internal val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
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

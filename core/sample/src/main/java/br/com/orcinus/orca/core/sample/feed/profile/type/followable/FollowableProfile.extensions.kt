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

package br.com.orcinus.orca.core.sample.feed.profile.type.followable

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileWriter
import br.com.orcinus.orca.core.sample.feed.profile.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider

/**
 * Creates a sample [FollowableProfile].
 *
 * @param writer [SampleProfileWriter] by which write operations to the [FollowableProfile] will be
 *   performed.
 * @param postProvider [SamplePostProvider] by which the [FollowableProfile]'s [Post]s will be
 *   provided.
 * @param follow Current [Follow] status.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   images will be loaded from a [SampleImageSource].
 */
fun <T : Follow> FollowableProfile.Companion.createSample(
  writer: SampleProfileWriter,
  postProvider: SamplePostProvider,
  follow: T,
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): FollowableProfile<T> {
  val delegate = Profile.createSample(postProvider, imageLoaderProvider)
  return SampleFollowableProfile(
    delegate.id,
    delegate.account,
    delegate.avatarLoader,
    delegate.name,
    delegate.bio,
    follow,
    delegate.followerCount,
    delegate.followingCount,
    delegate.url,
    writer,
    postProvider
  )
}

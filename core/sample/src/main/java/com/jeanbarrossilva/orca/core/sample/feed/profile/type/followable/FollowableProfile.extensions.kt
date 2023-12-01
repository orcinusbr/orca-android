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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [FollowableProfile].
 *
 * @param writer [SampleProfileWriter] by which write operations to the [FollowableProfile] will be
 *   performed.
 * @param postProvider [SamplePostProvider] by which the [FollowableProfile]'s [Post]s will be
 *   provided.
 * @param follow Current [Follow] status.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun <T : Follow> FollowableProfile.Companion.createSample(
  writer: SampleProfileWriter,
  postProvider: SamplePostProvider,
  follow: T,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
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

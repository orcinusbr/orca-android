package com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [FollowableProfile].
 *
 * @param writer [SampleProfileWriter] by which write operations to the [FollowableProfile] will be
 *   performed.
 * @param tootProvider [SampleTootProvider] by which the [FollowableProfile]'s [Toot]s will be
 *   provided.
 * @param follow Current [Follow] status.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun <T : Follow> FollowableProfile.Companion.createSample(
  writer: SampleProfileWriter,
  tootProvider: SampleTootProvider,
  follow: T,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): FollowableProfile<T> {
  val delegate = Profile.createSample(tootProvider, imageLoaderProvider)
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
    tootProvider
  )
}

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [EditableProfile].
 *
 * @param writer [SampleProfileWriter] used by the [EditableProfile]'s
 *   [editor][EditableProfile.editor] can make modifications.
 * @param tootProvider [SampleTootProvider] by which the [EditableProfile]'s [Toot]s will be loaded.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun EditableProfile.Companion.createSample(
  writer: SampleProfileWriter,
  tootProvider: SampleTootProvider,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): EditableProfile {
  val delegate = Profile.createSample(tootProvider, imageLoaderProvider)
  return SampleEditableProfile(
    delegate.id,
    delegate.account,
    delegate.avatarLoader,
    delegate.name,
    delegate.bio,
    delegate.followerCount,
    delegate.followingCount,
    delegate.url,
    tootProvider,
    writer
  )
}

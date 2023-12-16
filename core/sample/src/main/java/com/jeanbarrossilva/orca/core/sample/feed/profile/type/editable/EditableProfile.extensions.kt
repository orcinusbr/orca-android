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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader

/**
 * Creates a sample [EditableProfile].
 *
 * @param writer [SampleProfileWriter] used by the [EditableProfile]'s
 *   [editor][EditableProfile.editor] can make modifications.
 * @param postProvider [SamplePostProvider] by which the [EditableProfile]'s [Post]s will be loaded.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun EditableProfile.Companion.createSample(
  writer: SampleProfileWriter,
  postProvider: SamplePostProvider,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): EditableProfile {
  val delegate = Profile.createSample(postProvider, imageLoaderProvider)
  return SampleEditableProfile(
    delegate.id,
    delegate.account,
    delegate.avatarLoader,
    delegate.name,
    delegate.bio,
    delegate.followerCount,
    delegate.followingCount,
    delegate.url,
    postProvider,
    writer
  )
}

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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

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

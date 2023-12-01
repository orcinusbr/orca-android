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

package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [ProfileSearchResult].
 *
 * @param avatarImageLoader [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [ProfileSearchResult]'s avatar will be loaded.
 */
fun ProfileSearchResult.Companion.createSample(
  avatarImageLoader: ImageLoader.Provider<SampleImageSource>
): ProfileSearchResult {
  val author = Author.createSample(avatarImageLoader)
  return ProfileSearchResult(
    author.id,
    author.account,
    author.avatarLoader,
    author.name,
    author.profileURL
  )
}

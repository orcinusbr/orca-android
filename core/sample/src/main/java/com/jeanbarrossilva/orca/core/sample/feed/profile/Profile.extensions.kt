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

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * Creates a sample [Profile].
 *
 * @param postProvider [SamplePostProvider] by which the [Profile]'s [Post]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   images will be loaded from a [SampleImageSource].
 */
fun Profile.Companion.createSample(
  postProvider: SamplePostProvider,
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Profile {
  val author = Author.createSample(imageLoaderProvider)
  return object : SampleProfile {
    override val id = author.id
    override val account = author.account
    override val avatarLoader = author.avatarLoader
    override val name = author.name
    override val bio =
      StyledString(
        "Co-founder @ Grupo Estoa, software engineer, author, writer and content creator; " +
          "neuroscience, quantum physics and philosophy enthusiast."
      )
    override val followerCount = 1_024
    override val followingCount = 64
    override val url = author.profileURL
    override val postProvider = postProvider
  }
}

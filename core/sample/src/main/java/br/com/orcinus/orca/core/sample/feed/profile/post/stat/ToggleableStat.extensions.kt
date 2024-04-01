/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.post.stat

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.sample.feed.profile.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Creates a sample [ToggleableStat].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] by which an [ImageLoader] for loading images
 *   from a [SampleImageSource] will be provided.
 * @param count Initial amount of elements.
 */
internal fun Posts.Builder.AdditionScope.createSampleToggleableStat(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>,
  count: Int = 0
): ToggleableStat<Profile> {
  return ToggleableStat(count) {
    val profilesFlow = MutableStateFlow(emptyList<Profile>())
    var profiles by profilesFlow
    val postProvider by lazy { writerProvider.provide().postProvider }
    val profile by lazy { Profile.createSample(postProvider, imageLoaderProvider) }
    get { profilesFlow }
    onSetEnabled { isEnabled -> if (isEnabled) profiles += profile else profiles -= profile }
  }
}

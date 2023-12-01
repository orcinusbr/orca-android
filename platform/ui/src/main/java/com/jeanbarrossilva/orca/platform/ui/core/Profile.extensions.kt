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

package com.jeanbarrossilva.orca.platform.ui.core

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/** Creates a platform-specific sample [Profile]. */
@Composable
internal fun Profile.Companion.createSample(): Profile {
  val imageLoaderProvider = ImageLoader.Provider.createSample()
  val postProvider = Instance.createSample(imageLoaderProvider).postProvider
  return Profile.createSample(postProvider, imageLoaderProvider)
}

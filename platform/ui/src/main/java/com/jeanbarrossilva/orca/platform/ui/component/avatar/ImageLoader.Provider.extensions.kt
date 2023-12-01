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

package com.jeanbarrossilva.orca.platform.ui.component.avatar

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.ui.core.image.PlatformSampleImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 */
@Composable
fun ImageLoader.Provider.Companion.createSample(): ImageLoader.Provider<SampleImageSource> {
  return createSample(LocalContext.current)
}

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 *
 * @param context [Context] with which the underlying [PlatformSampleImageLoader.Provider] will be
 *   instantiated.
 */
fun ImageLoader.Provider.Companion.createSample(
  context: Context
): ImageLoader.Provider<SampleImageSource> {
  return PlatformSampleImageLoader.Provider(context)
}

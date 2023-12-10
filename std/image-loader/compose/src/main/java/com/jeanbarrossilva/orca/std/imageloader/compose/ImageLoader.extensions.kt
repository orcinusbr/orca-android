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

package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.coil.CoilImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.LocalImageLoader
import java.net.URL

/**
 * Remembers an [ImageLoader].
 *
 * @param source Resource ID from which the [Image] will be obtained.
 */
@Composable
fun rememberImageLoader(@DrawableRes source: Int): SomeImageLoader {
  val context = LocalContext.current
  return remember(context, source) {
    object : LocalImageLoader() {
      override val context = context
      override val source = source
    }
  }
}

/**
 * Remembers an [ImageLoader].
 *
 * @param source [URL] from which the [Image] will be obtained.
 */
@Composable
fun rememberImageLoader(source: URL): SomeImageLoader {
  val context = LocalContext.current
  return remember(context) { CoilImageLoader.Provider(context).provide(source) }
}

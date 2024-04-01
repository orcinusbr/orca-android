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

package br.com.orcinus.orca.std.image.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import br.com.orcinus.orca.std.image.compose.local.LocalImageLoader
import java.net.URL

/**
 * Remembers a [ComposableImageLoader].
 *
 * @param source Resource ID from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(@DrawableRes source: Int): SomeComposableImageLoader {
  return remember(source) { LocalImageLoader(source) }
}

/**
 * Remembers a [ComposableImageLoader].
 *
 * @param source [URL] from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(source: URL): SomeComposableImageLoader {
  return remember(source) { AsyncImageLoader.Provider.provide(source) }
}

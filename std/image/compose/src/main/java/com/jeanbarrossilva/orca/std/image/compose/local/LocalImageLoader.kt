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

package com.jeanbarrossilva.orca.std.image.compose.local

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.compose.ComposableImage
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [ImageLoader] that loads an image locally through its resource ID. */
internal class LocalImageLoader(override val source: Int) :
  ComposableImageLoader<@receiver:DrawableRes Int>() {
  override fun load(): ComposableImage {
    return { contentDescription, shape, contentScale, modifier ->
      Image(
        painterResource(source),
        contentDescription,
        modifier.clip(shape),
        contentScale = contentScale
      )
    }
  }
}

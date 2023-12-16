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

package com.jeanbarrossilva.orca.std.image.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.local.drawable.toImage

/** [ImageLoader] that loads an [Image] locally through its resource ID. */
abstract class LocalImageLoader : ImageLoader<Int> {
  /** [Context] through which the underlying [Drawable] will be obtained. */
  protected abstract val context: Context

  @get:DrawableRes abstract override val source: Int

  override suspend fun load(size: ImageLoader.Size): Image? {
    return context.getDrawable(source)?.toImage(size)
  }
}

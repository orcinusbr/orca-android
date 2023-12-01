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

package com.jeanbarrossilva.orca.std.imageloader.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [ImageLoader] that loads an [Image] locally through its resource ID.
 *
 * @param context [Context] through which the underlying [Drawable] will be obtained.
 */
abstract class LocalImageLoader : ImageLoader<Int> {
  protected abstract val context: Context
  @get:DrawableRes abstract override val source: Int

  override suspend fun load(width: Int, height: Int): Image? {
    return context.getDrawable(source)?.toBitmap(width, height)?.toImage()
  }
}

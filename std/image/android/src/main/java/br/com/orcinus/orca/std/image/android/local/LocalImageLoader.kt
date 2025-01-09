/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.std.image.android.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import java.lang.ref.WeakReference

/**
 * [ImageLoader] that loads an image locally through its resource ID.
 *
 * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
 *   image is obtained.
 */
internal class LocalImageLoader(
  private val contextRef: WeakReference<Context>,
  @DrawableRes override val source: Int
) : AndroidImageLoader<Int>() {
  override fun load() =
    createImage()
      .asDrawable { contextRef.get()?.let { ContextCompat.getDrawable(it, source) } }
      .asComposable { modifier, contentDescription, shape, contentScale ->
        Image(
          painterResource(source),
          contentDescription,
          modifier.clip(shape),
          contentScale = contentScale
        )
      }
}

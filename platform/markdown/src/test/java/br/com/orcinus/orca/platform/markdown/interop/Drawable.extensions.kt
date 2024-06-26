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

package br.com.orcinus.orca.platform.markdown.interop

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.PaintDrawable
import androidx.annotation.ColorInt

/**
 * ARGB color that's been applied, or [Color.TRANSPARENT] in case the color by which the receiver
 * [Drawable] has been tinted is either nonexistent or cannot be obtained through the current
 * implementation of this computed property.
 */
internal val Drawable.color
  @ColorInt
  get() =
    when (this) {
      is BitmapDrawable -> paint?.color
      is ColorDrawable -> color
      is GradientDrawable -> color?.defaultColor
      is PaintDrawable -> paint?.color
      else -> null
    }
      ?: Color.TRANSPARENT

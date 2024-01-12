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

package com.jeanbarrossilva.orca.platform.core.image

import androidx.annotation.DrawableRes
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.core.R

/** Resource ID for this respective [SampleImageSource]. */
internal val SampleImageSource.resourceID
  @DrawableRes
  get() =
    when (this) {
      AuthorImageSource.Default -> R.drawable.sample_avatar_default
      AuthorImageSource.Christian -> R.drawable.sample_avatar_christian
      AuthorImageSource.Rambo -> R.drawable.sample_avatar_rambo
      CoverImageSource.Default -> R.drawable.sample_cover_default
      CoverImageSource.PixelPals -> R.drawable.sample_cover_pixel_pals
      SampleImageSource.None -> -1
    }

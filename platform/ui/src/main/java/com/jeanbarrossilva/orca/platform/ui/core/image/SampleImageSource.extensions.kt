package com.jeanbarrossilva.orca.platform.ui.core.image

import androidx.annotation.DrawableRes
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.ui.R

/** Resource ID for this respective [SampleImageSource]. */
internal val SampleImageSource.resourceID
  @DrawableRes
  get() =
    when (this) {
      com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource.Default ->
        R.drawable.sample_avatar_default
      com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource.Rambo ->
        R.drawable.sample_avatar_rambo
      com.jeanbarrossilva.orca.core.sample.image.CoverImageSource.Default ->
        R.drawable.sample_cover_default
      SampleImageSource.None -> -1
    }

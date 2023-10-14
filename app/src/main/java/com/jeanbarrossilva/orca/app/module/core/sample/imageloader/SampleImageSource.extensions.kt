package com.jeanbarrossilva.orca.app.module.core.sample.imageloader

import androidx.annotation.DrawableRes
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.SampleImageSource

/** Resource ID for this respective [SampleImageSource]. */
internal val SampleImageSource.resourceID
  @DrawableRes
  get() =
    when (this) {
      AuthorImageSource.Default -> R.drawable.sample_avatar_default
      AuthorImageSource.Rambo -> R.drawable.sample_avatar_rambo
      CoverImageSource.Default -> R.drawable.sample_cover_default
    }

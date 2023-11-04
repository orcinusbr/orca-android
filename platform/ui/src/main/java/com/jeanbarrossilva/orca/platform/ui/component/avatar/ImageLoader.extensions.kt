package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/** Creates an [ImageLoader] that loads the avatar of a default sample [Author]. */
@Composable
internal fun ImageLoader.Companion.forDefaultSampleAuthor(): ImageLoader<SampleImageSource> {
  return ImageLoader.Provider.createSample().provide(AuthorImageSource.Default)
}

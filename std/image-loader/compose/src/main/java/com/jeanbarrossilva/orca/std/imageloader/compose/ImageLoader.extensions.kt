package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.net.URL

/**
 * Remembers an [ImageLoader].
 *
 * @param source Source from which the [Image] will be obtained.
 */
@Composable
internal fun rememberImageLoader(source: URL): SomeImageLoader {
  val context = LocalContext.current
  return remember(context) { CoilImageLoader.Provider(context).provide(source) }
}

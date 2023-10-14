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
 * @param url [URL] of the [Image] to be loaded.
 */
@Composable
fun rememberImageLoader(url: URL): SomeImageLoader {
  val context = LocalContext.current
  return remember(context) { CoilImageLoader(context, url) }
}

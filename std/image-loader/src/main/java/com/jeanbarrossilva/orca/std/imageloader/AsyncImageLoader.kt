package com.jeanbarrossilva.orca.std.imageloader

import java.net.URL

/** [ImageLoader] that asynchronously loads [Image]s from a [URL]. */
abstract class AsyncImageLoader : ImageLoader {
  /** [URL] of the [Image] to be loaded. */
  protected abstract val url: URL
}

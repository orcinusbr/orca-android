package com.jeanbarrossilva.orca.std.imageloader

import java.net.URL

/** Loads [Image]s asynchronously through [load]. */
interface ImageLoader {
  /**
   * Loads the [Image] to which the [url] leads.
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   * @param url [URL] of the [Image] to be loaded.
   */
  suspend fun load(width: Int, height: Int, url: URL): Image?
}

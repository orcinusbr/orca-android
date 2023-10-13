package com.jeanbarrossilva.orca.std.imageloader

/** Loads [Image]s asynchronously through [load]. */
interface ImageLoader {
  /**
   * Loads an [Image].
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   */
  suspend fun load(width: Int, height: Int): Image?
}

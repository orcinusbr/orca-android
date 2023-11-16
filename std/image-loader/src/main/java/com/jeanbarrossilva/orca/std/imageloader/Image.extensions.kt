package com.jeanbarrossilva.orca.std.imageloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * Builds an [Image].
 *
 * @param width How wide the [Image] is.
 * @param height How tall the [Image] is.
 * @param build Configures the [Image] to be built.
 */
suspend fun buildImage(width: Int, height: Int, build: Image.Builder.() -> Unit): Image {
  return withContext(Dispatchers.IO) {
    coroutineScope { Image.Builder(width, height).apply(build).build() }
  }
}

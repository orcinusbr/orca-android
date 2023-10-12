package com.jeanbarrossilva.orca.std.imageloader

/**
 * Builds an [Image].
 *
 * @param width How wide the [Image] is.
 * @param height How tall the [Image] is.
 * @param build Configures the [Image] to be built.
 */
fun buildImage(width: Int, height: Int, build: Image.Builder.() -> Unit): Image {
  return Image.Builder(width, height).apply(build).build()
}

package com.jeanbarrossilva.orca.std.imageloader

/** [ImageLoader] with a generic source. */
typealias SomeImageLoader = ImageLoader<*>

/** [ImageLoader.Provider] with a generic source. */
typealias SomeImageLoaderProvider = ImageLoader.Provider<*>

/**
 * Loads an [Image] through [load].
 *
 * @param T Source from which the [Image] will be obtained.
 */
interface ImageLoader<T : Any> {
  /** Source from which the [Image] will be obtained. */
  val source: T

  /**
   * Provides an [ImageLoader] through [provide].
   *
   * @param T Source from which the [Image] will be obtained.
   */
  fun interface Provider<T : Any> {
    /**
     * Provides an [ImageLoader].
     *
     * @param source Source from which the [Image] will be obtained.
     */
    fun provide(source: T): ImageLoader<T>

    companion object
  }

  /**
   * Loads an [Image].
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   */
  suspend fun load(width: Int, height: Int): Image?

  companion object
}

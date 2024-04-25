/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image

import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

/**
 * Factory that creates [ImageLoader.Provider]s for various types of source.
 *
 * @see createForSampleImageSource
 * @see createForURI
 */
abstract class ImageLoaderProviderFactory {
  /**
   * [IllegalArgumentException] thrown if an image source type is unknown.
   *
   * @param sourceName Name of the unknown source.
   */
  internal class UnknownSourceTypeException(sourceName: String) :
    IllegalArgumentException("$sourceName is not a known image source type.")

  /**
   * Creates an [ImageLoader.Provider] that provides an [ImageLoader] by which an image is loaded
   * through a sample image source.
   */
  abstract fun createForSampleImageSource(): SomeImageLoaderProvider<Any>

  /**
   * Creates an [ImageLoader.Provider] that provides an [ImageLoader] by which an image is loaded
   * through a [URI].
   */
  abstract fun createForURI(): SomeImageLoaderProvider<URI>

  /**
   * Creates an [ImageLoader.Provider] for the [sourceClass].
   *
   * @param sourceClass [KClass] of the source from which an image can be loaded.
   * @throws UnknownSourceTypeException If the type of source is unknown and, therefore, doesn't
   *   have a declared [ImageLoader.Provider] creator method for it within this
   *   [ImageLoaderProviderFactory].
   * @see createForSampleImageSource
   * @see createForURI
   */
  @Throws(UnknownSourceTypeException::class)
  internal fun createFor(sourceClass: KClass<*>): SomeImageLoaderProvider<Any> {
    @Suppress("UNCHECKED_CAST")
    return fold(sourceClass, ::createForURI, ::createForSampleImageSource)
      as SomeImageLoaderProvider<Any>
  }

  companion object {
    /**
     * Whether this is the [KClass] of the source of a sample image. Ultimately, because the actual
     * type is not accessible from within this module, checks if the receiver or any of its
     * superclasses is named "SampleImageSource".
     */
    private val KClass<*>.isOfSampleImageSource
      get() = (allSuperclasses + this).map(KClass<*>::simpleName).any("SampleImageSource"::equals)

    /** Whether this is the name of a sample image source class. */
    private val String.isSampleImageSourceClassName
      get() = startsWith("br.com.orcinus.orca.core.sample.image.")

    /** Whether this [String] is the representation of an [URI]. */
    private val String.isOfURI
      get() =
        try {
          URL(this)
          true
        } catch (_: MalformedURLException) {
          false
        }

    /**
     * Performs one of the given actions ([onURI] or [onSampleImageSource]) if the type of source is
     * equivalent to that to which these lambdas refer to.
     *
     * @param T Value returned by the lambdas.
     * @param sourceClass [KClass] of the source from which an image can be loaded.
     * @param onURI Operation to be run if the source is a [URI].
     * @param onSampleImageSource Callback executed if the source is that of a sample image.
     * @throws UnknownSourceTypeException If the source type is unknown.
     * @see KClass.isOfSampleImageSource
     */
    @Throws(UnknownSourceTypeException::class)
    internal fun <T> fold(sourceClass: KClass<*>, onURI: () -> T, onSampleImageSource: () -> T): T {
      return when {
        sourceClass == URI::class -> onURI()
        sourceClass.isOfSampleImageSource -> onSampleImageSource()
        else -> throw UnknownSourceTypeException(sourceClass.java.simpleName)
      }
    }

    /**
     * Performs one of the given actions ([onURI] or [onSampleImageSource]) if the given [String]
     * representation of the source is equivalent to that to which these lambdas refer to.
     *
     * @param I Sample image source.
     * @param O Value returned by the lambdas.
     * @param source Source from which an image can be loaded, represented as a [String].
     * @param onURI Operation to be run if the source is a [URI].
     * @param onSampleImageSource Callback executed if the source is that of a sample image.
     * @throws ClassCastException If the source is that of a sample image and has a type other than
     *   [I].
     * @throws NoSuchFieldException If the source is that of a sample image and doesn't have an
     *   object instance.
     * @throws UnknownSourceTypeException If the source type is unknown.
     */
    @Throws(
      ClassCastException::class,
      NoSuchFieldException::class,
      UnknownSourceTypeException::class
    )
    internal fun <I, O> fold(source: String, onURI: (URI) -> O, onSampleImageSource: (I) -> O): O {
      return when {
        source.isOfURI -> onURI(URI(source))
        source.isSampleImageSourceClassName ->
          @Suppress("UNCHECKED_CAST") onSampleImageSource(getObjectInstanceOrThrow(source) as I)
        else -> throw UnknownSourceTypeException(source)
      }
    }

    /**
     * Obtains the object instance of the [KClass] whose name is the given one.
     *
     * @param className Qualified name of the [KClass] whose object instance will be returned.
     * @throws NoSuchFieldException If the [KClass] doesn't have an object instance.
     */
    @Throws(NoSuchFieldException::class)
    private fun getObjectInstanceOrThrow(className: String): Any {
      return Class.forName(className).kotlin.objectInstance
        ?: throw NoSuchFieldException("$className is not a Kotlin object.")
    }
  }
}

package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [InstanceProvider].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun InstanceProvider.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): InstanceProvider {
  return object : InstanceProvider {
    override fun provide(): SomeInstance {
      return Instance.createSample(imageLoaderProvider)
    }
  }
}

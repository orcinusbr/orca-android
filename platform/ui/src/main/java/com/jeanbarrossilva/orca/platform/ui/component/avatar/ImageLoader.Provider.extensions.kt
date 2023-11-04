package com.jeanbarrossilva.orca.platform.ui.component.avatar

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.ui.core.image.PlatformSampleImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 */
@Composable
fun ImageLoader.Provider.Companion.createSample(): ImageLoader.Provider<SampleImageSource> {
  return createSample(LocalContext.current)
}

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 *
 * @param context [Context] with which the underlying [PlatformSampleImageLoader.Provider] will be
 *   instantiated.
 */
fun ImageLoader.Provider.Companion.createSample(
  context: Context
): ImageLoader.Provider<SampleImageSource> {
  return PlatformSampleImageLoader.Provider(context)
}

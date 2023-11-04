package com.jeanbarrossilva.orca.platform.ui.core.image

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.toImage

internal class PlatformSampleImageLoader
private constructor(private val context: Context, override val source: SampleImageSource) :
  ImageLoader<SampleImageSource> {
  class Provider(private val context: Context) : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): PlatformSampleImageLoader {
      return PlatformSampleImageLoader(context, source)
    }
  }

  override suspend fun load(width: Int, height: Int): Image? {
    return ContextCompat.getDrawable(context, source.resourceID)?.toBitmap(width, height)?.toImage()
  }
}

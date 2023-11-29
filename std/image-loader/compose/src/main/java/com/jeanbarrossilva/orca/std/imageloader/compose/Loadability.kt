package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntSize
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.loadable
import com.jeanbarrossilva.orca.std.imageloader.AsyncImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import kotlinx.coroutines.runBlocking

/** Indicates how an [Image] can be loaded through an [ImageLoader]. */
internal sealed class Loadability {
  /** [ImageLoader] by which the [Image] will be loaded. */
  protected abstract val loader: SomeImageLoader

  /** Size of the [Image]. */
  protected abstract val size: IntSize

  /**
   * [Image] can only be loaded asynchronously. Denotes that a blocking call to [ImageLoader.load]
   * might not be as appropriate as it would be if the [Loadability] was [Instant].
   */
  private class Async(override val loader: SomeImageLoader, override val size: IntSize) :
    Loadability() {
    @Composable
    override fun get(): Loadable<Bitmap> {
      var loadable by remember { mutableStateOf<Loadable<Bitmap>>(Loadable.Loading()) }
      LaunchedEffect(size, loader) {
        loadable = loader.load(size.width, size.height)?.toBitmap().loadable()!!
      }
      return loadable
    }
  }

  /** [Image] can be loaded blockingly, since a network call won't be made. */
  private class Instant(override val loader: SomeImageLoader, override val size: IntSize) :
    Loadability() {
    @Composable
    override fun get(): Loadable<Bitmap> {
      return runBlocking { loader.load(size.width, size.height)?.toBitmap() }.loadable()
        ?: Loadable.Failed(IllegalStateException("Could not load image instantly."))
    }
  }

  /**
   * [Image] currently doesn't have the ability to be loaded because of the environment that it's
   * in: either because it is supposed to be loaded asynchronously but is currently being previewed,
   * or its size hasn't been properly specified.
   */
  private class None(override val loader: SomeImageLoader, override val size: IntSize) :
    Loadability() {
    @Composable
    override fun get(): Loadable<Bitmap> {
      return Loadable.Failed(UnsupportedOperationException("Image cannot be loaded."))
    }
  }

  /**
   * Gets the [Loadable] that wraps the [Image], loaded according to its environment constraints.
   */
  @Composable abstract fun get(): Loadable<Bitmap>

  companion object {
    /**
     * Automatically infers what the [Loadability] that suits the [loader] is.
     *
     * @param loader [ImageLoader] by which the [Image] will be loaded.
     * @param size Size of the [Image].
     */
    @Composable
    fun of(loader: SomeImageLoader, size: IntSize): Loadability {
      return when {
        loader is AsyncImageLoader && LocalInspectionMode.current -> None(loader, size)
        loader is AsyncImageLoader -> Async(loader, size)
        else -> Instant(loader, size)
      }
    }
  }
}

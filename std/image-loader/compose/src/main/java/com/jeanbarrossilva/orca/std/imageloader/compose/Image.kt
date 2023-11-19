package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.loadable
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image as _Image
import java.net.URL

/**
 * [Composable] that loads an [Image][com.jeanbarrossilva.orca.std.imageloader.Image] with the
 * specified [loader].
 *
 * @param loader [ImageLoader] by which the underlying
 *   [Image][com.jeanbarrossilva.orca.std.imageloader.Image] will be loaded.
 * @param contentDescription Description of what the image contains.
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param loader [ImageLoader] by which the image will be loaded.
 * @param shape [Shape] by which this [Image][_Image] will be clipped.
 * @param contentScale Defines how the image will be scaled within this [Composable]'s bounds.
 */
@Composable
fun Image(
  loader: SomeImageLoader,
  contentDescription: String,
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  contentScale: ContentScale = ContentScale.Fit
) {
  val isInspecting = LocalInspectionMode.current
  var size by remember { mutableStateOf<IntSize?>(null) }
  val canLoadImage = remember(isInspecting, size) { !isInspecting || size != null }
  var bitmapLoadable by
    remember(canLoadImage) {
      mutableStateOf<Loadable<ImageBitmap>>(
        if (canLoadImage) {
          Loadable.Loading()
        } else {
          Loadable.Failed(UnsupportedOperationException("Image cannot be loaded."))
        }
      )
    }

  LaunchedEffect(size, canLoadImage, loader) {
    if (canLoadImage) {
      size?.let {
        bitmapLoadable = loader.load(it.width, it.height)?.toBitmap()?.asImageBitmap().loadable()!!
      }
    }
  }

  Placeholder(
    modifier.onPlaced { size = it.size },
    isLoading = bitmapLoadable is Loadable.Loading,
    shape
  ) {
    CompositionLocalProvider(
      LocalContentColor provides contentColorFor(PlaceholderDefaults.color)
    ) {
      BoxWithConstraints(Modifier.matchParentSize(), Alignment.Center) {
        bitmapLoadable.let {
          if (it is Loadable.Loaded) {
            Image(
              it.content,
              contentDescription,
              Modifier.clip(shape).matchParentSize(),
              contentScale = contentScale
            )
          } else if (it is Loadable.Failed) {
            Box(Modifier.clip(shape).background(PlaceholderDefaults.color).matchParentSize())

            Icon(
              AutosTheme.iconography.unavailable.filled.asImageVector,
              contentDescription = "Unavailable image",
              Modifier.height(maxHeight / 2).width(maxWidth / 2)
            )
          }
        }
      }
    }
  }
}

/** Preview of an [Image][_Image]. */
@Composable
@MultiThemePreview
private fun ImagePreview() {
  AutosTheme {
    _Image(
      rememberImageLoader(URL("https://images.unsplash.com/photo-1692890846581-da1a95435f34")),
      contentDescription = "Preview image",
      Modifier.size(128.dp)
    )
  }
}

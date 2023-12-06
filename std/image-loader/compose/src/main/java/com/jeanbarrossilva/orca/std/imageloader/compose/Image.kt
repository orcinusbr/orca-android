/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.map
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image as _Image
import com.jeanbarrossilva.orca.std.imageloader.local.LocalImageLoader

/**
 * [Composable] that loads an [Image][com.jeanbarrossilva.orca.std.imageloader.Image] with the
 * specified [loader].
 *
 * @param loader [ImageLoader] by which the underlying
 *   [Image][com.jeanbarrossilva.orca.std.imageloader.Image] will be loaded.
 * @param contentDescription Description of what the image contains.
 * @param modifier [Modifier] to be applied to the underlying [BoxWithConstraints].
 * @param sizing [Sizing] that defines how the underlying
 *   [Image][com.jeanbarrossilva.orca.std.imageloader.Image] will be sized.
 * @param shape [Shape] by which this [Image][_Image] will be clipped.
 * @param contentScale Defines how the image will be scaled within this [Composable]'s bounds.
 */
@Composable
fun Image(
  loader: SomeImageLoader,
  contentDescription: String,
  modifier: Modifier = Modifier,
  sizing: Sizing = Sizing.Constrained,
  shape: Shape = RectangleShape,
  contentScale: ContentScale = ContentScale.None
) {
  BoxWithConstraints(
    modifier.semantics {
      this.contentDescription = contentDescription
      role = Role.Image
    }
  ) {
    val size = remember(sizing, constraints) { sizing.size(constraints) }
    val bitmapLoadable = Loadability.of(loader, size).get().map(Bitmap::asImageBitmap)

    Placeholder(Modifier.matchParentSize(), isLoading = bitmapLoadable is Loadable.Loading, shape) {
      CompositionLocalProvider(
        LocalContentColor provides contentColorFor(PlaceholderDefaults.color)
      ) {
        bitmapLoadable.let {
          if (it is Loadable.Loaded) {
            Image(
              it.content,
              contentDescription,
              Modifier.clip(shape).matchParentSize().clearAndSetSemantics {},
              contentScale = contentScale
            )
          } else if (it is Loadable.Failed) {
            Box(Modifier.clip(shape).background(PlaceholderDefaults.color).matchParentSize())

            Icon(
              AutosTheme.iconography.unavailable.filled.asImageVector,
              contentDescription = "Unavailable image",
              Modifier.align(Alignment.Center)
                .height(this@BoxWithConstraints.maxHeight / 2)
                .width(this@BoxWithConstraints.maxWidth / 2)
            )
          }
        }
      }
    }
  }
}

/**
 * [Composable] that loads an [Image][com.jeanbarrossilva.orca.std.imageloader.Image] with a
 * [LocalImageLoader].
 *
 * @param modifier [Modifier] to be applied to the underlying [BoxWithConstraints].
 * @param loader [ImageLoader] by which the underlying
 *   [Image][com.jeanbarrossilva.orca.std.imageloader.Image] will be loaded.
 * @param sizing [Sizing] that defines how the underlying
 *   [Image][com.jeanbarrossilva.orca.std.imageloader.Image] will be sized.
 */
@Composable
internal fun Image(
  modifier: Modifier = Modifier,
  loader: SomeImageLoader =
    object : LocalImageLoader() {
      override val context = LocalContext.current
      override val source = R.drawable.image
    },
  sizing: Sizing = Sizing.Constrained,
) {
  _Image(loader, contentDescription = "Preview image", modifier.size(512.dp), sizing)
}

/** Preview of an [Image] that has failed loading. */
@Composable
@MultiThemePreview
private fun FailedImagePreview() {
  AutosTheme { _Image(loader = rememberImageLoader(Attachment.sample.url)) }
}

/**
 * Preview of a constrained [Image][_Image].
 *
 * @see Sizing.Constrained
 */
@Composable
@Preview
private fun ConstrainedImagePreview() {
  AutosTheme { _Image(sizing = Sizing.Constrained) }
}

/**
 * Preview of a widened [Image][_Image].
 *
 * @see Sizing.Widened
 */
@Composable
@Preview
private fun WidenedImagePreview() {
  AutosTheme { _Image(sizing = Sizing.Widened) }
}

/**
 * Preview of an elongated [Image][_Image].
 *
 * @see Sizing.Elongated
 */
@Composable
@Preview
private fun ElongatedImagePreview() {
  AutosTheme { _Image(sizing = Sizing.Elongated) }
}

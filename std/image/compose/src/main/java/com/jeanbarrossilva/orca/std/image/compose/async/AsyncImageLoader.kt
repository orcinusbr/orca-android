/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.std.image.compose.async

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.compose.ComposableImage
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.orca.std.image.compose.R
import java.net.URL

/** [ComposableImageLoader] that loads an image asynchronously. */
class AsyncImageLoader private constructor(override val source: URL) :
  ComposableImageLoader<URL>() {
  /** [ImageLoader.Provider] that provides an [AsyncImageLoader]. */
  object Provider : ImageLoader.Provider<URL, ComposableImage> {
    override fun provide(source: URL): AsyncImageLoader {
      return AsyncImageLoader(source)
    }
  }

  override fun load(): ComposableImage {
    return { contentDescription, shape, contentScale, modifier ->
      var state by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Loading(painter = null))
      }

      BoxWithConstraints {
        Placeholder(
          modifier.semantics {
            this.contentDescription = contentDescription
            role = Role.Image
          },
          state is AsyncImagePainter.State.Loading,
          shape
        ) {
          AsyncImage(
            "$source",
            contentDescription,
            Modifier.clip(shape).fillMaxSize().clearAndSetSemantics {},
            onState = { state = it },
            contentScale = contentScale
          )
        }

        if (state is AsyncImagePainter.State.Error) {
          Box(Modifier.clip(shape).background(PlaceholderDefaults.color).matchParentSize()) {
            Icon(
              AutosTheme.iconography.unavailable.filled.asImageVector,
              contentDescription =
                stringResource(R.string.std_image_compose_async_image_unavailable),
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

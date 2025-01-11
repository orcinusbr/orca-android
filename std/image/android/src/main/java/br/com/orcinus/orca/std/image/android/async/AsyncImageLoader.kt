/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.std.image.android.async

import android.content.Context
import android.graphics.drawable.Drawable
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
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.image.android.R
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import java.lang.ref.WeakReference
import java.net.URI
import java.util.Objects

/**
 * [AndroidImageLoader] that loads an image asynchronously.
 *
 * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
 *   image is obtained.
 */
open class AsyncImageLoader
internal constructor(private val contextRef: WeakReference<Context>, override val source: URI) :
  AndroidImageLoader<URI>() {
  /**
   * [ImageLoader.Provider] that provides an [AsyncImageLoader].
   *
   * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
   *   image is obtained.
   */
  class Provider(private val contextRef: WeakReference<Context>) :
    AndroidImageLoader.Provider<URI> {
    override fun provide(source: URI): AsyncImageLoader {
      return AsyncImageLoader(contextRef, source)
    }
  }

  override fun equals(other: Any?): Boolean {
    return other is AsyncImageLoader && contextRef == other.contextRef && source == other.source
  }

  override fun hashCode(): Int {
    return Objects.hash(contextRef, source)
  }

  override fun load() =
    createImage().asComposable { modifier, contentDescription, shape, contentScale ->
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

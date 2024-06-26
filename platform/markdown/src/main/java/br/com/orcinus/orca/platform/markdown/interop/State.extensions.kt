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

package br.com.orcinus.orca.platform.markdown.interop

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.doOnLayout

/**
 * Converts [f] into a [Drawable] and returns a [State] containing it.
 *
 * @param f [Composable] to be made drawable.
 */
@Composable
internal fun drawableStateOf(f: @Composable () -> Unit): State<Drawable?> {
  val drawableAsState = remember(f) { mutableStateOf<Drawable?>(null) }

  AndroidView(::ComposeView, Modifier.drawWithContent {}) { view ->
    view.setContent(f)
    view.doOnLayout { laidOutView ->
      val (width, height) = laidOutView.width to laidOutView.height
      if (width > 0 && height > 0) {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        laidOutView.layout(laidOutView.left, laidOutView.top, laidOutView.right, laidOutView.bottom)
        try {
          laidOutView.draw(canvas)
        } catch (_: NullPointerException) {}
        drawableAsState.value = bitmap.toDrawable(laidOutView.resources)
      }
    }
  }

  return drawableAsState
}

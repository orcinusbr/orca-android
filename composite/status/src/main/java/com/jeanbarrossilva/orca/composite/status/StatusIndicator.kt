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

package com.jeanbarrossilva.orca.composite.status

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.platform.autos.R
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Contained icon for facilitating the understanding of what a given [Status] means.
 *
 * @param iconResource [Drawable] resource of the icon.
 * @param containerColor [Color] by which the container with the icon will be colored.
 * @param contentColor [Color] of the icon itself.
 * @param modifier [Modifier] that is applied to the [Canvas].
 */
@Composable
internal fun StatusIndicator(
  @DrawableRes iconResource: Int,
  containerColor: Color,
  contentColor: Color,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val contentColorInArgb = remember(contentColor, contentColor::toArgb)
  val icon =
    remember(context, iconResource, contentColorInArgb) {
      ContextCompat.getDrawable(context, iconResource)
        .let(::checkNotNull)
        .apply { setTint(contentColorInArgb) }
        .toBitmap()
        .asImageBitmap()
    }

  Canvas(modifier.size(24.dp)) {
    drawCircle(containerColor)
    scale(.7f) { drawImage(icon) }
  }
}

/** Preview of a [StatusIndicator]. */
@Composable
@Preview
private fun StatusIndicatorPreview() {
  AutosTheme {
    StatusIndicator(
      R.drawable.icon_profile_filled,
      containerColor = AutosTheme.colors.activation.favorite.asColor,
      contentColor = Color.White
    )
  }
}

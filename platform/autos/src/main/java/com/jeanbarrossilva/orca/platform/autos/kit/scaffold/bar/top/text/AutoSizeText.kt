/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.AutoSizeRange
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.AutoSizer
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.rememberAutoSizeRange
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [Text] that is sized automatically based on the specified [range].
 *
 * @param text [String] to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Text].
 * @param style [TextStyle] with which it will be styled.
 * @param range [AutoSizeRange] within which the font size should be.
 */
@Composable
fun AutoSizeText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = LocalTextStyle.current,
  range: AutoSizeRange = rememberAutoSizeRange(style.fontSize)
) {
  val density = LocalDensity.current
  var canBeDrawn by remember { mutableStateOf(false) }
  val sizer = remember(density, range) { AutoSizer(density, range) }

  Text(
    text,
    modifier.drawWithContent {
      if (canBeDrawn) {
        drawContent()
      }
    },
    fontSize = sizer.size,
    onTextLayout = { sizer.autoSize(it, canBeDrawn) { canBeDrawn = true } },
    maxLines = 1,
    style = style
  )
}

/** Preview of an [AutoSizeText]. */
@Composable
@MultiThemePreview
private fun AutoSizeTextPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { AutoSizeText("Text") }
  }
}

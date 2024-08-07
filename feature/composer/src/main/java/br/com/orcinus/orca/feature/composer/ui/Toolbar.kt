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

package br.com.orcinus.orca.feature.composer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

internal const val COMPOSER_TOOLBAR = "composer-toolbar"
internal const val COMPOSER_TOOLBAR_BOLD_FORMAT = "composer-toolbar-bold-format"
internal const val COMPOSER_TOOLBAR_ITALIC_FORMAT = "composer-toolbar-italic-format"

@Composable
internal fun Toolbar(
  isBold: Boolean,
  onBoldToggle: (isBold: Boolean) -> Unit,
  isItalicized: Boolean,
  onItalicToggle: (isItalicized: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val shape = AutosTheme.forms.large.asShape
  val spacing = AutosTheme.spacings.small.dp

  CompositionLocalProvider(LocalContentColor provides AutosTheme.colors.surface.content.asColor) {
    LazyRow(
      modifier
        .shadow(4.dp, shape)
        .clip(shape)
        .background(AutosTheme.colors.surface.container.asColor)
        .height(56.dp)
        .testTag(COMPOSER_TOOLBAR),
      horizontalArrangement = Arrangement.spacedBy(spacing),
      verticalAlignment = Alignment.CenterVertically,
      contentPadding = PaddingValues(horizontal = spacing)
    ) {
      item {
        FormatIconButton(
          isEnabled = isBold,
          onClick = { onBoldToggle(!isBold) },
          Modifier.testTag(COMPOSER_TOOLBAR_BOLD_FORMAT)
        ) {
          Icon(Icons.Rounded.FormatBold, contentDescription = "Bold")
        }
      }

      item {
        FormatIconButton(
          isEnabled = isItalicized,
          onClick = { onItalicToggle(!isItalicized) },
          Modifier.testTag(COMPOSER_TOOLBAR_ITALIC_FORMAT)
        ) {
          Icon(Icons.Rounded.FormatItalic, contentDescription = "Italic")
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun ToolbarPreview() {
  AutosTheme {
    Toolbar(isBold = true, onBoldToggle = {}, isItalicized = false, onItalicToggle = {})
  }
}

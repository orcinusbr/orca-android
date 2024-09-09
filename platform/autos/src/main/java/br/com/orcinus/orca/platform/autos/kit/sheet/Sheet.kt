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

package br.com.orcinus.orca.platform.autos.kit.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.colors.LocalContainerColor
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [Sheet] for testing purposes. */
const val SheetTag = "sheet"

/**
 * Component that overlays the entire UI, intended to display important, relevant information.
 *
 * @param modifier [Modifier] to be applied to the underlying [ModalBottomSheet].
 * @param content Content to be shown in this [Sheet].
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Sheet(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  CompositionLocalProvider(
    LocalContainerColor provides AutosTheme.colors.background.container.asColor,
    LocalWindowInsets provides WindowInsets.Zero
  ) {
    ModalBottomSheet(
      onDismissRequest = {},
      modifier.statusBarsPadding().testTag(SheetTag),
      containerColor = LocalContainerColor.current,
      tonalElevation = 0.dp,
      windowInsets = LocalWindowInsets.current
    ) {
      content()
    }
  }
}

/** Preview of a [Sheet]. */
@Composable
@MultiThemePreview
private fun SheetPreview() {
  AutosTheme {
    @OptIn(ExperimentalMaterial3Api::class)
    Sheet {
      Scaffold(topAppBar = { TopAppBar(title = { AutoSizeText("Title") }) }) {
        Box(
          Modifier.clip(AutosTheme.forms.large.asShape)
            .padding(it)
            .background(AutosTheme.colors.surface.container.asColor)
            .fillMaxSize(),
          Alignment.Center
        ) {
          Text("Content", style = AutosTheme.typography.titleMedium)
        }
      }
    }
  }
}

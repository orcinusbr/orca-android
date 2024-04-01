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

package br.com.orcinus.orca.platform.autos.kit.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/**
 * [Popup] menu that displays a variety of options through [DropdownMenuItem]s in its [content].
 *
 * An Orca-specific version of [androidx.compose.material3.DropdownMenu].
 *
 * @param isExpanded Whether it's being shown.
 * @param onDismissal Callback run when it is requested to be dismissed.
 * @param modifier [Modifier] to be applied to the underlying [DropdownMenu].
 * @param content [DropdownMenuItem]s contained by this
 *   [DropdownMenu][br.com.orcinus.orca.platform.ui.component.menu.DropdownMenu].
 */
@Composable
fun DropdownMenu(
  isExpanded: Boolean,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit
) {
  DropdownMenu(
    isExpanded,
    onDismissal,
    modifier.background(AutosTheme.colors.surface.container.asColor),
    content = content
  )
}

/** Preview of a [DropdownMenu][br.com.orcinus.orca.platform.ui.component.menu.DropdownMenu]. */
@Composable
@MultiThemePreview
private fun DropdownMenuPreview() {
  AutosTheme {
    DropdownMenu(isExpanded = true, onDismissal = {}) {
      repeat(8) { DropdownMenuItem(text = { Text("Item $it") }, onClick = {}) }
    }
  }
}

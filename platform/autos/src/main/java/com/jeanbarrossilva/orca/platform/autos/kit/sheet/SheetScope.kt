/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.autos.kit.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/** Scope through which the UI of a [Sheet] can be built. */
class SheetScope internal constructor() {
  /** Title to be shown in the [TopAppBar]. */
  private var title = @Composable {}

  /** Content to be shown as the [Scaffold]'s. */
  private var content: @Composable () -> Unit = {}

  /**
   * Defines the title to be shown in the [TopAppBar].
   *
   * @param title [Text] to be displayed as the title.
   */
  fun title(title: @Composable () -> Unit) {
    this.title = title
  }

  /**
   * Defines the content to be shown as the [Scaffold]'s.
   *
   * @param content [Composable] to be displayed below the [title].
   */
  fun content(content: @Composable () -> Unit) {
    this.content = content
  }

  /**
   * UI composed according to the configuration given to this [SheetScope].
   *
   * @param modifier [Modifier] to be applied to the underlying [Scaffold].
   */
  @Composable
  internal fun Content(modifier: Modifier = Modifier) {
    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
      modifier,
      topAppBar = {
        TopAppBar(
          title,
          defaultContainerColor = SheetDefaults.containerColor,
          windowInsets = WindowInsets(left = 0.dp, top = 0.dp, right = 0.dp, bottom = 0.dp),
          scrollBehavior = null
        )
      },
      containerColor = SheetDefaults.containerColor
    ) {
      Box(Modifier.padding(it + AutosTheme.spacings.medium.dp)) { content() }
    }
  }
}

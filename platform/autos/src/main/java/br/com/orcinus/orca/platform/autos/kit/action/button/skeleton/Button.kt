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

package br.com.orcinus.orca.platform.autos.kit.action.button.skeleton

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Default values of [Button]s. */
internal object ButtonDefaults {
  /** [Shape] by which a [Button] is clipped by default. */
  val shape
    @Composable get() = AutosTheme.forms.large.asShape

  /** [PaddingValues] that pads the content of a [Button] by default. */
  val padding
    @Composable get() = PaddingValues(AutosTheme.spacings.large.dp)
}

/**
 * Orca-specific button skeleton that provides a [ButtonScope] to its [content] through which the
 * loading state of the [Composable] that composes this one can be managed.
 *
 * [ButtonScope] offers two methods suited for operations that are common to all Orca [Button]s:
 * [ButtonScope.Loadable], by which the main, loaded content of the [Button] (i. e., its [Text]
 * label) will be shown; and [ButtonScope.load], that indicates whether the action for which the
 * [Button] is is currently being executed, replacing the content specified within the
 * [ButtonScope.Loadable] by a loading indicator until it has finished.
 *
 * @param content Content to be shown, with the [ButtonScope] by which common operations can be
 *   performed.
 */
@Composable
internal fun Button(content: @Composable ButtonScope.() -> Unit) {
  remember(::ButtonScope).content()
}

/** Preview of [Button] whose content is loading. */
@Composable
@MultiThemePreview
private fun LoadingButtonPreview() {
  AutosTheme { Button { load { Loadable {} } } }
}

/** Preview of [Button] whose content is loaded. */
@Composable
@MultiThemePreview
private fun LoadedButtonPreview() {
  AutosTheme { Button { Loadable { Text("Hello, world!") } } }
}

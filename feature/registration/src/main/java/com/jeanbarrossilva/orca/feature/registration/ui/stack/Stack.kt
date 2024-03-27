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

package com.jeanbarrossilva.orca.feature.registration.ui.stack

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.jeanbarrossilva.orca.feature.registration.ui.status.Status
import com.jeanbarrossilva.orca.feature.registration.ui.status.StatusCard
import com.jeanbarrossilva.orca.feature.registration.ui.status.rememberStatusCardState
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * Stacks each of its added items on top of each other, with the one that has been added the most
 * recently being the totally visible one (resembling the behavior of an actual [java.util.Stack]).
 *
 * Realistically, only a certain amount of items will be rendered: the one added lastly (referred to
 * as the "foreground" one) and some of those previous to it (the "background" ones). The exact
 * quantity is determined by [StackMeasurePolicy.MaxVisibleItemCount].
 *
 * For a more detailed explanation on the distinction between foreground and background items and
 * the reasoning behind how they're indexed, refer to
 * [StackMeasurePolicy.requireBackgroundItemIndex]'s documentation.
 *
 * @param modifier [Modifier] that is applied to the underlying [Layout].
 * @param content Configures the items to be shown and that can be added through the receiver
 *   [StackScope].
 */
@Composable
internal fun Stack(modifier: Modifier = Modifier, content: StackScope.() -> Unit) {
  val scope = remember(::StackScope)

  DisposableEffect(scope, content) {
    scope.content()
    onDispose {}
  }

  Layout({ scope.contents().forEach { it() } }, modifier, StackMeasurePolicy)
}

/** Preview of a [Stack]. */
@Composable
@MultiThemePreview
private fun StackPreview() {
  AutosTheme {
    Stack {
      item { StatusCard(rememberStatusCardState(Status.Failed)) { Text("Card #1") } }
      item { StatusCard(rememberStatusCardState(Status.Failed)) { Text("Card #2") } }
      item { StatusCard(rememberStatusCardState(Status.Succeeded)) { Text("Card #3") } }
    }
  }
}

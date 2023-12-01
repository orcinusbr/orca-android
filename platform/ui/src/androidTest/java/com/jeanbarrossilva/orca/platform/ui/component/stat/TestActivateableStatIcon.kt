/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

@Composable
@Suppress("TestFunctionName")
internal fun TestActivateableStatIcon(
  modifier: Modifier = Modifier,
  isActive: Boolean = false,
  interactiveness: ActivateableStatIconInteractiveness = ActivateableStatIconInteractiveness.Still
) {
  ActivateableStatIcon(
    AutosTheme.iconography.forward.asImageVector,
    contentDescription = "Proceed",
    isActive,
    interactiveness,
    ActivateableStatIconColors(LocalContentColor.current, LocalContentColor.current),
    modifier
  )
}

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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * [Color]s by which a [ButtonBar] may be colored.
 *
 * @param idleContainer [Color] by which the container should be colored when idle.
 * @param scrolledContainer [Color] by which the container should be colored when scrolled.
 */
class ButtonBarColors
internal constructor(private val idleContainer: Color, private val scrolledContainer: Color) {
  /**
   * Returns the [Color] by which a [ButtonBar]'s container should be colored.
   *
   * @param isIdle Whether the lazy list to which the [ButtonBar] is associated is scrolled.
   */
  @Composable
  internal fun container(isIdle: Boolean): Color {
    return animateColorAsState(
        if (isIdle) idleContainer else scrolledContainer,
        label = "ButtonBarContainerColor"
      )
      .value
  }

  companion object {
    /** [Color] by which an idle container is colored by default. */
    internal val defaultIdleContainer
      @Composable get() = AutosTheme.colors.background.container.asColor
  }
}

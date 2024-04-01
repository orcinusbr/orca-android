/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.stat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

/** Position in which a [Stat] is laid out. */
internal enum class StatPosition {
  /** [StatPosition] of the first [Stat]. */
  Leading {
    override val padding
      @Composable get() = PaddingValues(end = AutosTheme.spacings.small.dp)
  },

  /** [StatPosition] of a [Stat] that is neither the first nor the last one. */
  Subsequent {
    override val padding
      @Composable get() = PaddingValues(horizontal = AutosTheme.spacings.small.dp)
  },

  /** [StatPosition] of the last [Stat]. */
  Trailing {
    override val padding
      @Composable get() = PaddingValues(start = AutosTheme.spacings.small.dp)
  };

  /** Padding to be applied to the [Stat]. */
  @get:Composable abstract val padding: PaddingValues
}

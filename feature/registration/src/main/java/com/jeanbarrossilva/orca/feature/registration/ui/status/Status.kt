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

package com.jeanbarrossilva.orca.feature.registration.ui.status

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

@Immutable
internal enum class Status {
  Loading {
    @Composable
    override fun Indicator(modifier: Modifier) {
      CircularProgressIndicator(
        Modifier.size(24.dp),
        AutosTheme.colors.secondary.asColor,
        strokeCap = StrokeCap.Round
      )
    }

    @Composable
    override fun Description(modifier: Modifier) {
      Text("Attempting to register account...")
    }
  },
  Succeeded {
    @Composable
    override fun Indicator(modifier: Modifier) {
      StatusIndicator(
        com.jeanbarrossilva.orca.platform.autos.R.drawable.icon_selected,
        AutosTheme.colors.activation.reposted.asColor,
        Color.White
      )
    }

    @Composable
    override fun Description(modifier: Modifier) {
      Text("Account registered successfully.")
    }
  },
  Failed {
    @Composable
    override fun Indicator(modifier: Modifier) {
      StatusIndicator(
        com.jeanbarrossilva.orca.platform.autos.R.drawable.icon_close,
        AutosTheme.colors.activation.favorite.asColor,
        Color.White
      )
    }

    @Composable
    override fun Description(modifier: Modifier) {
      Text("Couldn't register account.")
    }
  };

  @Composable
  fun Indicator() {
    Indicator(Modifier)
  }

  @Composable
  fun Description() {
    Description(Modifier)
  }

  @Composable abstract fun Indicator(modifier: Modifier)

  @Composable abstract fun Description(modifier: Modifier)
}

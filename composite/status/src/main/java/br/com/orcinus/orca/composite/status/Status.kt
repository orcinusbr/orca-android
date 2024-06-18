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

package br.com.orcinus.orca.composite.status

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

/** Account registration stage that a [StatusCard] can represent. */
@Immutable
enum class Status {
  /** Denotes that an [Instance] at which an account can be registered is being searched for. */
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
      Text(stringResource(R.string.composite_status_loading))
    }
  },

  /**
   * Denotes that an available [Instance] has been found and an account been registered
   * successfully.
   */
  Succeeded {
    @Composable
    override fun Indicator(modifier: Modifier) {
      StatusIndicator(
        br.com.orcinus.orca.platform.autos.R.drawable.icon_selected,
        AutosTheme.colors.activation.reposted.asColor,
        Color.White
      )
    }

    @Composable
    override fun Description(modifier: Modifier) {
      Text(stringResource(R.string.composite_status_succeeded))
    }
  },

  /** Denotes that an account couldn't be registered at a given [Instance]. */
  Failed {
    @Composable
    override fun Indicator(modifier: Modifier) {
      StatusIndicator(
        br.com.orcinus.orca.platform.autos.R.drawable.icon_close,
        AutosTheme.colors.activation.favorite.asColor,
        Color.White
      )
    }

    @Composable
    override fun Description(modifier: Modifier) {
      Text(stringResource(R.string.composite_status_failed))
    }
  };

  /**
   * [StatusIndicator] that matches the [Description] and helps to easily identify whether an
   * account could be registered or if the process itself hasn't yet completed.
   */
  @Composable
  fun Indicator() {
    Indicator(Modifier)
  }

  /**
   * [Text] that clearly describes whether an account has been registered, hasn't or the process is
   * still ongoing.
   */
  @Composable
  fun Description() {
    Description(Modifier)
  }

  /**
   * [StatusIndicator] that matches the [Description] and helps to easily identify whether an
   * account could be registered or if the process itself hasn't yet completed.
   *
   * @param modifier [Modifier] that is applied to the [StatusIndicator].
   */
  @Composable abstract fun Indicator(modifier: Modifier)

  /**
   * [Text] that clearly describes whether an account has been registered, hasn't or the process is
   * still ongoing.
   *
   * @param modifier [Modifier] that is applied to the [Text].
   */
  @Composable abstract fun Description(modifier: Modifier)
}

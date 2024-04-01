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

package com.jeanbarrossilva.orca.composite.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.composite.status.state.StatusCardState
import com.jeanbarrossilva.orca.composite.status.state.rememberStatusCardState
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [Card] for displaying the progress of an account registration process.
 *
 * @param state [StatusCardState] that holds the current operation [Status].
 * @param modifier [Modifier] that is applied to the [Card].
 * @param title Message shown alongside an indicator and a description of what the current [Status]
 *   is.
 * @see rememberStatusCardState
 * @see Status.Indicator
 * @see Status.Description
 */
@Composable
fun StatusCard(
  state: StatusCardState,
  modifier: Modifier = Modifier,
  title: @Composable () -> Unit
) {
  val spacing = AutosTheme.spacings.large.dp

  Card(
    modifier,
    colors = CardDefaults.outlinedCardColors(),
    border = AutosTheme.borders.default.asBorderStroke
  ) {
    Row(
      Modifier.padding(spacing).fillMaxWidth(),
      Arrangement.spacedBy(spacing),
      Alignment.CenterVertically
    ) {
      state.status.Indicator()

      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.small.dp)) {
        ProvideTextStyle(AutosTheme.typography.titleMedium, title)
        ProvideTextStyle(AutosTheme.typography.labelMedium) { state.status.Description() }
      }
    }
  }
}

/**
 * Preview of a [StatusCard] with a loading [Status].
 *
 * @see Status.Loading
 */
@Composable
@MultiThemePreview
private fun LoadingStatusCardPreview() {
  AutosTheme { StatusCard(rememberStatusCardState()) { Text("${Domain.sample}") } }
}

/**
 * Preview of a [StatusCard] with a succeeded [Status].
 *
 * @see Status.Succeeded
 */
@Composable
@MultiThemePreview
private fun SucceededStatusCardPreview() {
  AutosTheme { StatusCard(rememberStatusCardState(Status.Succeeded)) { Text("${Domain.sample}") } }
}

/**
 * Preview of a [StatusCard] with a failed [Status].
 *
 * @see Status.Failed
 */
@Composable
@MultiThemePreview
private fun FailedStatusCardPreview() {
  AutosTheme { StatusCard(rememberStatusCardState(Status.Failed)) { Text("${Domain.sample}") } }
}

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

package com.jeanbarrossilva.orca.feature.postdetails.ui.header.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.feature.postdetails.PostDetails
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.Stat
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.StatDefaults
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon

@Composable
internal fun ReblogStat(details: PostDetails, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val isActive = remember(details, details::isReblogged)
  val contentColor by
    animateColorAsState(
      if (isActive) AutosTheme.colors.activation.reposted.asColor else StatDefaults.contentColor,
      label = "ContentColor"
    )

  Stat(contentColor = contentColor) {
    ReblogStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      modifier.size(24.dp)
    )

    Text(details.formattedReblogCount)
  }
}

@Composable
@MultiThemePreview
private fun InactiveReblogStatPreview() {
  AutosTheme { ReblogStat(PostDetails.sample.copy(isReblogged = false), onClick = {}) }
}

@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme { ReblogStat(PostDetails.sample.copy(isReblogged = true), onClick = {}) }
}

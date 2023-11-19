package com.jeanbarrossilva.orca.feature.tootdetails.ui.header.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetails
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.Stat
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.StatDefaults
import com.jeanbarrossilva.orca.platform.autos.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon

@Composable
internal fun ReblogStat(details: TootDetails, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
  AutosTheme { ReblogStat(TootDetails.sample.copy(isReblogged = false), onClick = {}) }
}

@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme { ReblogStat(TootDetails.sample.copy(isReblogged = true), onClick = {}) }
}

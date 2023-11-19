package com.jeanbarrossilva.orca.feature.composer.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector

@Composable
internal fun FormatIconButton(
  isEnabled: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val role = Role.Switch
  val contentColor by
    animateColorAsState(
      if (isEnabled) OrcaTheme.colors.primary.container.asColor else LocalContentColor.current,
      label = "ContentColor"
    )

  Box(
    modifier
      .clip(OrcaTheme.forms.small.asShape)
      .clickable(role = role, onClick = onClick)
      .padding(4.dp)
      .size(24.dp)
      .semantics {
        this.role = role
        toggleableState = if (isEnabled) ToggleableState.On else ToggleableState.Off
      },
    Alignment.Center
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
  }
}

@Composable
@MultiThemePreview
private fun DisabledFormatIconButtonPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      FormatIconButton(isEnabled = false)
    }
  }
}

@Composable
@MultiThemePreview
private fun EnabledFormatIconButtonPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      FormatIconButton(isEnabled = true)
    }
  }
}

@Composable
private fun FormatIconButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
  FormatIconButton(isEnabled, onClick = {}, modifier) {
    Icon(
      if (isEnabled) {
        OrcaTheme.iconography.compose.filled.asImageVector
      } else {
        OrcaTheme.iconography.compose.outlined.asImageVector
      },
      contentDescription = "Compose"
    )
  }
}

package com.jeanbarrossilva.orca.platform.theme.kit.action.button

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.IgnoringMutableInteractionSource
import com.jeanbarrossilva.orca.platform.theme.kit.action.Hoverable

/**
 * [IconButton] that gets visually highlighted when it's hovered.
 *
 * @param onClick Callback run whenever this [HoverableIconButton] is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 * @param content [Icon] to be shown.
 */
@Composable
fun HoverableIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val interactionSource = remember {
    IgnoringMutableInteractionSource(PressInteraction.Press::class, HoverInteraction::class)
  }

  Hoverable(modifier) {
    IconButton(onClick, interactionSource = interactionSource, content = content)
  }
}

@Composable
@MultiThemePreview
private fun HoverableIconButtonPreview() {
  OrcaTheme {
    HoverableIconButton(onClick = {}) {
      Icon(OrcaTheme.iconography.home.outlined, contentDescription = "Home")
    }
  }
}

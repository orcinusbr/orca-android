package com.jeanbarrossilva.orca.platform.autos.kit.action.button

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.extensions.IgnoringMutableInteractionSource
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

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
  AutosTheme {
    HoverableIconButton(onClick = {}) {
      Icon(AutosTheme.iconography.home.outlined.asImageVector, contentDescription = "Home")
    }
  }
}

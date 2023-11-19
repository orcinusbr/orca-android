package com.jeanbarrossilva.orca.platform.theme.kit.action.setting

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.iconography.Iconography
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/** Scope through which either an [icon] or a [button] action can be added. */
class ActionScope internal constructor() {
  /** Content that's been set as the action. */
  internal var content: @Composable () -> Unit by mutableStateOf({})
    private set

  /**
   * Adds an [Icon].
   *
   * @param contentDescription Returns the description of what the [Icon] within the [IconButton]
   *   represents.
   * @param modifier [Modifier] to be applied to the [Icon].
   * @param vector Returns the [ImageVector] to be shown.
   */
  fun icon(
    contentDescription: @Composable () -> String,
    modifier: Modifier = Modifier,
    vector: @Composable Iconography.() -> ImageVector
  ) {
    content = { Icon(OrcaTheme.iconography.vector(), contentDescription(), modifier) }
  }

  /**
   * Adds an [IconButton].
   *
   * @param contentDescription Returns the description of what the [Icon] within the [IconButton]
   *   represents.
   * @param onClick Callback run whenever the [IconButton] is clicked.
   * @param modifier [Modifier] to be applied to the [IconButton].
   * @param vector Returns the [ImageVector] to be shown by the [Icon].
   */
  fun button(
    contentDescription: @Composable () -> String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    vector: @Composable Iconography.() -> ImageVector
  ) {
    content = {
      IconButton(onClick, modifier.offset(x = 12.dp)) {
        Icon(OrcaTheme.iconography.vector(), contentDescription())
      }
    }
  }
}

package com.jeanbarrossilva.orca.platform.autos.kit.action.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Default values of a [Setting]. */
internal object SettingDefaults {
  /** [CornerBasedShape] that clips a [Setting] by default. */
  val shape
    @Composable get() = AutosTheme.forms.large.asShape

  /** Size in [Dp]s of the default spacing of a [Setting]. */
  val spacing
    @Composable get() = AutosTheme.spacings.large.dp
}

/**
 * A configuration.
 *
 * @param label Short description of what it's for.
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param shape [Shape] by which it will be clipped.
 * @param onClick Callback run whenever it's clicked.
 * @param icon [Icon] that visually represents what it does.
 * @param action Portrays the result of invoking [onClick] or executes a related action.
 */
@Composable
internal fun Setting(
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = SettingDefaults.shape,
  onClick: () -> Unit = {},
  icon: @Composable () -> Unit = {},
  action: ActionScope.() -> Unit = {}
) {
  Surface(Modifier.fillMaxWidth(), shape) {
    Row(
      Modifier.clickable(onClick = onClick).padding(SettingDefaults.spacing).then(modifier),
      Arrangement.SpaceBetween,
      Alignment.CenterVertically
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(SettingDefaults.spacing),
        verticalAlignment = Alignment.CenterVertically
      ) {
        icon()

        ProvideTextStyle(
          AutosTheme.typography.labelMedium.copy(
            color = AutosTheme.colors.background.content.asColor
          ),
          label
        )
      }

      Spacer(Modifier.width(SettingDefaults.spacing))
      ActionScope().apply(action).content()
    }
  }
}

/** Preview of a [Setting]. */
@Composable
@MultiThemePreview
private fun SettingPreview() {
  AutosTheme {
    Setting(
      onClick = {},
      label = { Text("Label") },
      icon = {
        Icon(AutosTheme.iconography.home.filled.asImageVector, contentDescription = "Setting")
      }
    ) {
      icon(contentDescription = { "Expand" }, vector = { forward.asImageVector })
    }
  }
}

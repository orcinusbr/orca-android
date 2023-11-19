package com.jeanbarrossilva.orca.feature.tootdetails.ui.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.theme.kit.action.Hoverable

internal object StatDefaults {
  val contentColor
    @Composable get() = OrcaTheme.colors.secondary.asColor
}

@Composable
internal fun Stat(
  modifier: Modifier = Modifier,
  contentColor: Color = StatDefaults.contentColor,
  content: @Composable RowScope.() -> Unit
) {
  Hoverable(modifier) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.small.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides OrcaTheme.typography.bodySmall.copy(color = contentColor)
      ) {
        content()
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun StatPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      Stat {
        Icon(OrcaTheme.iconography.comment.outlined.asImageVector, contentDescription = "Comments")
        Text("8")
      }
    }
  }
}

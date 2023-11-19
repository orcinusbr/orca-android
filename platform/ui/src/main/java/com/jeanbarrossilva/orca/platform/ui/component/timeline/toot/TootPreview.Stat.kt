package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.theme.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults

internal object StatDefaults {
  val IconSize = ActivateableStatIconDefaults.Size
}

internal enum class StatPosition {
  LEADING {
    override val padding
      @Composable get() = PaddingValues(end = OrcaTheme.spacings.small.dp)
  },
  SUBSEQUENT {
    override val padding
      @Composable get() = PaddingValues(horizontal = OrcaTheme.spacings.small.dp)
  },
  TRAILING {
    override val padding
      @Composable get() = PaddingValues(start = OrcaTheme.spacings.small.dp)
  };

  @get:Composable abstract val padding: PaddingValues
}

@Composable
internal fun Stat(
  position: StatPosition,
  vector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  label: @Composable () -> Unit = {}
) {
  Stat(position, onClick, modifier) {
    Icon(vector, contentDescription, Modifier.size(StatDefaults.IconSize))
    label()
  }
}

@Composable
internal fun Stat(
  position: StatPosition,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable RowScope.() -> Unit
) {
  val spacing = OrcaTheme.spacings.small.dp
  val contentColor = OrcaTheme.colors.secondary.asColor

  Hoverable(modifier.padding(position.padding).clickable(role = Role.Button, onClick = onClick)) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(spacing),
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
internal fun LeadingStatPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) { Stat(StatPosition.LEADING) }
  }
}

@Composable
@MultiThemePreview
internal fun SubsequentStatPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) { Stat(StatPosition.SUBSEQUENT) }
  }
}

@Composable
@MultiThemePreview
internal fun TrailingStatPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) { Stat(StatPosition.TRAILING) }
  }
}

@Composable
private fun Stat(position: StatPosition, modifier: Modifier = Modifier) {
  Stat(
    position,
    OrcaTheme.iconography.comment.outlined.asImageVector,
    contentDescription = "Comment",
    onClick = {},
    modifier
  ) {
    Text("8")
  }
}

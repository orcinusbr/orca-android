package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_REBLOG_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.StatPosition

@Composable
internal fun ReblogStat(
  position: StatPosition,
  preview: PostPreview,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isActive = remember(preview, preview::isReblogged)

  Stat(position, onClick, modifier.testTag(POST_PREVIEW_REBLOG_COUNT_STAT_TAG)) {
    val contentColor by
      animateColorAsState(
        if (isActive) Color(0xFF81C784) else LocalContentColor.current,
        label = "ContentColor"
      )

    ReblogStatIcon(isActive, ActivateableStatIconInteractiveness.Interactive { onClick() })

    Text(preview.formattedReblogCount, color = contentColor)
  }
}

@Composable
@MultiThemePreview
private fun InactiveReblogStatPreview() {
  AutosTheme {
    ReblogStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isReblogged = false), onClick = {})
  }
}

@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme {
    ReblogStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isReblogged = true), onClick = {})
  }
}

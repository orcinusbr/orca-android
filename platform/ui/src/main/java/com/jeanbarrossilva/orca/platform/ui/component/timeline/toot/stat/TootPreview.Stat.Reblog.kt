package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIconDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.StatDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview

@Composable
internal fun ReblogStat(preview: TootPreview, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val isActive = remember(preview, preview::isReblogged)
    val containerColor by animateColorAsState(
        if (isActive) Color(0xFF81C784) else StatDefaults.containerColor,
        label = "ContainerColor"
    )

    Stat(onClick, modifier.testTag(TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG), containerColor) {
        ReblogStatIcon(
            isActive,
            ActivateableStatIconInteractiveness.Interactive { onClick() },
            colors = ReblogStatIconDefaults.colors(activeColor = Color.White)
        )

        Text(preview.formattedReblogCount)
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun InactiveReblogStatPreview() {
    OrcaTheme {
        ReblogStat(TootPreview.sample.copy(isReblogged = false), onClick = { })
    }
}

@Composable
@Preview
private fun ActiveReblogStatPreview() {
    OrcaTheme {
        ReblogStat(TootPreview.sample.copy(isReblogged = true), onClick = { })
    }
}

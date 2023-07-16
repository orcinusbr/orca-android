package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.ReblogStatIcon
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.ReblogStatIconDefaults

@Composable
internal fun ReblogStat(preview: TootPreview, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val containerColor by animateColorAsState(
        if (preview.isReblogged) Color(0xFF81C784) else StatDefaults.containerColor
    )

    Stat(
        onClick,
        modifier.testTag(TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG),
        containerColor
    ) {
        ReblogStatIcon(
            isActive = preview.isReblogged,
            ActivateableStatIconInteractiveness.Interactive { onClick() },
            colors = ReblogStatIconDefaults.colors(activeColor = Color.White)
        )

        Text(preview.formattedReblogCount)
    }
}

@Composable
@Preview
private fun InactiveReblogStatPreview() {
    MastodonteTheme {
        ReblogStat(TootPreview.sample.copy(isReblogged = false), onClick = { })
    }
}

@Composable
@Preview
private fun ActiveReblogStatPreview() {
    MastodonteTheme {
        ReblogStat(TootPreview.sample.copy(isReblogged = true), onClick = { })
    }
}

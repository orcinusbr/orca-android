package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.core.image.ImageProvider
import com.jeanbarrossilva.orca.platform.ui.core.image.rememberImageProvider

/** Tag that identifies a [HeadlineCard] for testing purposes. **/
const val HEADLINE_CARD_TAG = "headline-card"

@Composable
fun HeadlineCard(
    headline: Headline,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageProvider: ImageProvider = rememberImageProvider()
) {
    val shape = OrcaTheme.shapes.medium

    Column(
        Modifier
            .shadow(2.dp, shape)
            .clip(shape)
            .clickable(onClick = onClick)
            .background(OrcaTheme.colors.surface.container)
            .testTag(HEADLINE_CARD_TAG)
    ) {
        imageProvider.provide(
            headline.coverURL,
            contentDescription = "Cover of \"${headline.title}\"",
            onStateChange = { },
            modifier
                .aspectRatio(16f / 9f)
                .fillMaxWidth()
        )

        Column(
            Modifier.padding(OrcaTheme.spacings.medium),
            Arrangement.spacedBy(OrcaTheme.spacings.small)
        ) {
            ProvideTextStyle(OrcaTheme.typography.bodyLarge) {
                Text(headline.title)
            }

            headline.subtitle?.let {
                ProvideTextStyle(OrcaTheme.typography.bodySmall) {
                    Text(it, overflow = TextOverflow.Ellipsis, maxLines = 4)
                }
            }
        }
    }
}

@Composable
internal fun HeadlineCard(modifier: Modifier = Modifier, onClick: () -> Unit = { }) {
    HeadlineCard(Headline.sample, onClick, modifier)
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun HeadlineCardPreview() {
    OrcaTheme {
        HeadlineCard()
    }
}

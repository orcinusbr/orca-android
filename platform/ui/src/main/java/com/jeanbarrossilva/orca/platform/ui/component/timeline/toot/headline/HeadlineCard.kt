package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

/** Tag that identifies a [HeadlineCard] for testing purposes. **/
const val HEADLINE_CARD_TAG = "headline-card"

@Composable
fun HeadlineCard(
    headline: Headline,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = rememberImageLoader()
) {
    val shape = OrcaTheme.shapes.large

    Column(
        modifier
            .border(2.dp, OrcaTheme.colors.placeholder, shape)
            .clip(shape)
            .clickable(onClick = onClick)
            .background(OrcaTheme.colors.surface.container)
            .testTag(HEADLINE_CARD_TAG)
    ) {
        Image(
            headline.coverURL,
            contentDescription = "Cover of \"${headline.title}\"",
            Modifier
                .aspectRatio(16f / 9f)
                .fillMaxWidth(),
            imageLoader,
            contentScale = ContentScale.Crop
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
@MultiThemePreview
private fun HeadlineCardPreview() {
    OrcaTheme {
        HeadlineCard()
    }
}

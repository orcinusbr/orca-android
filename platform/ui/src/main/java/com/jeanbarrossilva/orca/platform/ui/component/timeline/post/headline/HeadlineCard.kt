package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.platform.autos.border
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image

/** Tag that identifies a [HeadlineCard] for testing purposes. */
const val HEADLINE_CARD_TAG = "headline-card"

@Composable
fun HeadlineCard(headline: Headline, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val shape = AutosTheme.forms.large.asShape
  val interactionSource = remember(::MutableInteractionSource)

  Hoverable(
    modifier
      .border(shape)
      .clip(shape)
      .clickable(interactionSource, LocalIndication.current, onClick = onClick)
      .background(AutosTheme.colors.surface.container.asColor)
      .testTag(HEADLINE_CARD_TAG)
  ) {
    Column {
      headline.coverLoader?.let {
        Image(
          it,
          contentDescription =
            stringResource(R.string.platform_ui_headline_card_cover, headline.title),
          Modifier.aspectRatio(16f / 9f).fillMaxWidth(),
          contentScale = ContentScale.Crop
        )
      }

      Column(
        Modifier.padding(AutosTheme.spacings.medium.dp),
        Arrangement.spacedBy(AutosTheme.spacings.small.dp)
      ) {
        ProvideTextStyle(AutosTheme.typography.bodyLarge) { Text(headline.title) }

        headline.subtitle?.let {
          ProvideTextStyle(AutosTheme.typography.bodySmall) {
            Text(it, overflow = TextOverflow.Ellipsis, maxLines = 4)
          }
        }
      }
    }
  }
}

@Composable
internal fun HeadlineCard(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
  HeadlineCard(Headline.createSample(ImageLoader.Provider.createSample()), onClick, modifier)
}

@Composable
@MultiThemePreview
private fun HeadlineCardPreview() {
  AutosTheme { HeadlineCard() }
}

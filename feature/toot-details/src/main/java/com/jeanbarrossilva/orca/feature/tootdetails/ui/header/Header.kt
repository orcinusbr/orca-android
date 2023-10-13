package com.jeanbarrossilva.orca.feature.tootdetails.ui.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.orca.feature.tootdetails.R
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetails
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.stat.FavoriteStat
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.stat.ReblogStat
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HeadlineCard
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

@Composable
internal fun Header(modifier: Modifier = Modifier) {
  Header(
    avatar = { SmallAvatar() },
    name = { SmallTextualPlaceholder() },
    username = { MediumTextualPlaceholder() },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)) {
        repeat(3) { LargeTextualPlaceholder() }
        MediumTextualPlaceholder()
      }
    },
    metadata = { SmallTextualPlaceholder() },
    stats = {},
    modifier
  )
}

@Composable
internal fun Header(
  details: TootDetails,
  onHighlightClick: () -> Unit,
  onFavorite: () -> Unit,
  onReblog: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  val avatarLoader = rememberImageLoader(details.avatarURL)

  Header(
    avatar = { SmallAvatar(avatarLoader, details.name) },
    name = { Text(details.name) },
    username = { Text(details.formattedUsername) },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium)) {
        Text(details.text)

        details.highlight?.headline?.let { HeadlineCard(it, onHighlightClick) }
      }
    },
    metadata = { Text(details.formattedPublicationDateTime) },
    stats = {
      Divider()

      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Stat {
          Icon(
            OrcaTheme.iconography.comment.outlined,
            contentDescription = stringResource(R.string.toot_details_comments)
          )
          Text(details.formattedCommentCount)
        }

        FavoriteStat(details, onClick = onFavorite)
        ReblogStat(details, onClick = onReblog)

        Stat {
          Icon(
            OrcaTheme.iconography.share.outlined,
            contentDescription = stringResource(R.string.toot_details_share),
            Modifier.clickable(
              remember(::MutableInteractionSource),
              indication = null,
              onClick = onShare
            )
          )
        }
      }

      Divider()
    },
    modifier
  )
}

@Composable
private fun Header(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  username: @Composable () -> Unit,
  content: @Composable () -> Unit,
  metadata: @Composable () -> Unit,
  stats: @Composable ColumnScope.() -> Unit,
  modifier: Modifier = Modifier
) {
  val spacing = OrcaTheme.spacings.large

  Column(modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(spacing),
      verticalAlignment = Alignment.CenterVertically
    ) {
      avatar()

      Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)) {
        ProvideTextStyle(OrcaTheme.typography.bodyLarge, name)
        ProvideTextStyle(OrcaTheme.typography.bodySmall, username)
      }
    }

    Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium)) {
      content()
      ProvideTextStyle(OrcaTheme.typography.bodySmall, metadata)
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium),
      horizontalAlignment = Alignment.CenterHorizontally,
      content = stats
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadingHeaderPreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container) { Header() } }
}

@Composable
@MultiThemePreview
private fun LoadedHeaderPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container) {
      Header(
        TootDetails.sample,
        onHighlightClick = {},
        onFavorite = {},
        onReblog = {},
        onShare = {}
      )
    }
  }
}

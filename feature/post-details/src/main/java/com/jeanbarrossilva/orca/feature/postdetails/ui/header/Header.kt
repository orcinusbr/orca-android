/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.postdetails.ui.header

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
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.orca.feature.postdetails.PostDetails
import com.jeanbarrossilva.orca.feature.postdetails.R
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.stat.FavoriteStat
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.stat.ReblogStat
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.avatar.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.link.LinkCard

@Composable
internal fun Header(modifier: Modifier = Modifier) {
  Header(
    avatar = { SmallAvatar() },
    name = { SmallTextualPlaceholder() },
    username = { MediumTextualPlaceholder() },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
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
  details: PostDetails,
  onHighlightClick: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  Header(
    avatar = { SmallAvatar(details.avatarLoader, details.name) },
    name = { Text(details.name) },
    username = { Text(details.formattedUsername) },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
        Text(details.text)

        details.highlight?.headline?.let { LinkCard(it, onHighlightClick) }
      }
    },
    metadata = { Text(details.formattedPublicationDateTime) },
    stats = {
      Divider()

      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Stat {
          Icon(
            AutosTheme.iconography.comment.outlined.asImageVector,
            contentDescription = stringResource(R.string.feature_post_details_comments)
          )
          Text(details.formattedCommentCount)
        }

        FavoriteStat(details, onClick = onFavorite)
        ReblogStat(details, onClick = onRepost)

        Stat {
          Icon(
            AutosTheme.iconography.share.outlined.asImageVector,
            contentDescription = stringResource(R.string.feature_post_details_share),
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
  val spacing = AutosTheme.spacings.large.dp

  Column(modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(spacing),
      verticalAlignment = Alignment.CenterVertically
    ) {
      avatar()

      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
        ProvideTextStyle(AutosTheme.typography.bodyLarge, name)
        ProvideTextStyle(AutosTheme.typography.bodySmall, username)
      }
    }

    Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
      content()
      ProvideTextStyle(AutosTheme.typography.bodySmall, metadata)
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.medium.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      content = stats
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadingHeaderPreview() {
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { Header() } }
}

@Composable
@MultiThemePreview
private fun LoadedHeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Header(
        PostDetails.sample,
        onHighlightClick = {},
        onFavorite = {},
        onRepost = {},
        onShare = {}
      )
    }
  }
}

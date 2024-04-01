/*
 * Copyright © 2023-2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.timeline.stat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.stat.Stat as _Stat
import br.com.orcinus.orca.composite.timeline.stat.activateable.favorite.FavoriteStat
import br.com.orcinus.orca.composite.timeline.stat.activateable.repost.RepostStat
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies [Stats]' comment [Stat][_Stat] for testing purposes. */
const val STATS_COMMENT_STAT_TAG = "stats-comment-stat"

/** Tag that identifies [Stats]' share [Stat][_Stat] for testing purposes. */
const val STATS_SHARE_STAT_TAG = "stats-share-stat"

/**
 * Actions for a [Post]'s [Stat]s.
 *
 * @param modifier [Modifier] to be applied to the underlying [Row].
 */
@Composable
fun Stats(modifier: Modifier = Modifier) {
  Stats(StatsDetails.sample, onComment = {}, onFavorite = {}, onRepost = {}, onShare = {}, modifier)
}

/**
 * Actions for a [Post]'s [Stat]s.
 *
 * @param details [StatsDetails] of the [Post]'s [Stat]s.
 * @param onFavorite Callback run whenever the [Post] is requested to be favorited.
 * @param onRepost Callback run whenever the [Post] is requested to be reblogged.
 * @param onShare Callback run whenever the [Post] is requested to be externally shared.
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param contentColor [Color] by which each [Stat][_Stat]s' contents will be colored.
 */
@Composable
fun Stats(
  details: StatsDetails,
  onComment: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier,
  contentColor: Color = StatDefaults.contentColor
) {
  Row(modifier, Arrangement.SpaceBetween) {
    _Stat(
      StatPosition.Leading,
      AutosTheme.iconography.comment.outlined.asImageVector,
      contentDescription = stringResource(R.string.composite_timeline_stat_comments),
      onClick = onComment,
      Modifier.testTag(STATS_COMMENT_STAT_TAG),
      contentColor
    ) {
      Text(details.formattedCommentCount)
    }

    FavoriteStat(
      StatPosition.Subsequent,
      details,
      onClick = onFavorite,
      inactiveContentColor = contentColor
    )

    RepostStat(
      StatPosition.Subsequent,
      details,
      onClick = onRepost,
      inactiveContentColor = contentColor
    )

    _Stat(
      StatPosition.Trailing,
      AutosTheme.iconography.share.outlined.asImageVector,
      contentDescription = stringResource(R.string.composite_timeline_stat_share),
      onClick = onShare,
      Modifier.testTag(STATS_SHARE_STAT_TAG),
      contentColor
    )
  }
}

/** Preview of [Stats]. */
@Composable
@MultiThemePreview
private fun StatsPreview() {
  AutosTheme { Stats() }
}

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

package br.com.orcinus.orca.composite.timeline.stat.activateable.repost

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import br.com.orcinus.orca.composite.timeline.stat.Stat
import br.com.orcinus.orca.composite.timeline.stat.StatDefaults
import br.com.orcinus.orca.composite.timeline.stat.StatPosition
import br.com.orcinus.orca.composite.timeline.stat.activateable.ActivateableStatIconInteractiveness
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

/** Tag that identifies a [RepostStat] for testing purposes. */
const val RepostStatTag = "repost-stat"

/**
 * [Stat] of a repost.
 *
 * @param position [StatPosition] that indicates how it is positioned.
 * @param details [StatsDetails] from which the activeness and the count will be obtained.
 * @param onClick Callback run whenever it is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Stat].
 * @param inactiveContentColor [Color] by which the content will be colored when in an inactive
 *   state.
 */
@Composable
internal fun RepostStat(
  position: StatPosition,
  details: StatsDetails,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  inactiveContentColor: Color = StatDefaults.contentColor
) {
  val isActive = remember(details, details::isReposted)

  Stat(position, onClick, modifier.testTag(RepostStatTag)) {
    val contentColor by
      animateColorAsState(
        if (isActive) AutosTheme.colors.activation.reposted.asColor else inactiveContentColor,
        label = "ContentColor"
      )

    RepostStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      Modifier.size(StatDefaults.IconSize),
      RepostStatIconDefaults.colors(inactiveColor = inactiveContentColor)
    )

    Text(details.formattedReblogCount, color = contentColor)
  }
}

/** Preview of an inactive [RepostStat]. */
@Composable
@MultiThemePreview
private fun InactiveReblogStatPreview() {
  AutosTheme {
    RepostStat(
      StatPosition.Subsequent,
      StatsDetails.createSample(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider
        )
        .copy(isReposted = false),
      onClick = {}
    )
  }
}

/** Preview of an active [RepostStat]. */
@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme {
    RepostStat(
      StatPosition.Subsequent,
      StatsDetails.createSample(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider
        )
        .copy(isReposted = true),
      onClick = {}
    )
  }
}

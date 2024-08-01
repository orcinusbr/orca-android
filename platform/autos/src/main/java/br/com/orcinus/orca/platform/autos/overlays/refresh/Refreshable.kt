/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.overlays.refresh

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi

/** Tag that identifies a [Refreshable]'s [PullRefreshIndicator] for testing purposes. */
@InternalPlatformAutosApi const val RefreshIndicatorTag = "refreshable-refresh-indicator"

/**
 * Allows for a refresh to be requested from the [content].
 *
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @param content Content that can be refreshed.
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Refreshable(refresh: Refresh, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  val pullRefreshState = rememberPullRefreshState(refresh.isInProgress, refresh.listener::onRefresh)

  Box(modifier.pullRefresh(pullRefreshState)) {
    content()

    PullRefreshIndicator(
      refresh.isInProgress,
      pullRefreshState,
      Modifier.align(Alignment.TopCenter).testTag(RefreshIndicatorTag).semantics {
        isInProgress = refresh.isInProgress
      }
    )
  }
}

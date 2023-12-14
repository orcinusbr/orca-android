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

package com.jeanbarrossilva.orca.feature.gallery.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.feature.gallery.GalleryBoundary
import com.jeanbarrossilva.orca.feature.gallery.GalleryViewModel
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatsDetails
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing

internal const val GALLERY_PAGER_TAG = "gallery-pager-tag"

@Composable
fun Gallery(
  viewModel: GalleryViewModel,
  boundary: GalleryBoundary,
  entrypointIndex: Int,
  secondary: List<Attachment>,
  modifier: Modifier = Modifier,
  entrypoint: @Composable (Modifier, Sizing) -> Unit
) {
  val statsDetails by viewModel.statsDetailsFlow.collectAsState(StatsDetails.Empty)

  Gallery(
    entrypointIndex,
    secondary,
    onDownload = viewModel::download,
    statsDetails,
    onComment = { boundary.navigateToPostDetails(viewModel.postID) },
    onFavorite = viewModel::toggleFavorite,
    onRepost = viewModel::toggleRepost,
    onShare = viewModel::share,
    onClose = boundary::pop,
    modifier,
    entrypoint
  )
}

@Composable
internal fun Gallery(
  modifier: Modifier = Modifier,
  onDownload: () -> Unit = {},
  onComment: () -> Unit = {},
  onFavorite: () -> Unit = {},
  onRepost: () -> Unit = {},
  onShare: () -> Unit = {},
  onClose: () -> Unit = {}
) {
  Gallery(
    entrypointIndex = 0,
    Attachment.samples,
    onDownload,
    StatsDetails.sample,
    onComment,
    onFavorite,
    onRepost,
    onShare,
    onClose,
    modifier
  ) { pageModifier, sizing ->
    SampleEntrypoint(sizing, pageModifier)
  }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun Gallery(
  entrypointIndex: Int,
  secondary: List<Attachment>,
  onDownload: () -> Unit,
  statsDetails: StatsDetails,
  onComment: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
  entrypoint: @Composable (Modifier, Sizing) -> Unit
) {
  var areActionsVisible by rememberSaveable { mutableStateOf(true) }
  var areOptionsVisible by rememberSaveable(areActionsVisible) { mutableStateOf(false) }
  val pagerState = rememberPagerState(pageCount = secondary.size::inc)
  val sizing = remember { Sizing.Widened }
  val pages =
    remember(entrypointIndex, secondary, sizing) {
      List<@Composable () -> Unit>(pagerState.pageCount) { index ->
        @Composable {
          Page(
            entrypointIndex,
            index,
            secondary,
            onActionsVisibilityToggle = { areActionsVisible = it },
            entrypoint = entrypoint
          )
        }
      }
    }

  Box(modifier.background(Color.Black)) {
    HorizontalPager(
      pagerState,
      Modifier.fillMaxHeight().testTag(GALLERY_PAGER_TAG),
      beyondBoundsPageCount = 1,
      verticalAlignment = Alignment.CenterVertically
    ) { page ->
      pages[page]()
    }

    AnimatedVisibility(areActionsVisible, enter = fadeIn(), exit = fadeOut()) {
      Actions(
        areOptionsVisible,
        onOptionsVisibilityToggle = { areOptionsVisible = it },
        onDownload,
        statsDetails,
        onComment,
        onFavorite,
        onRepost,
        onShare,
        onClose
      )
    }
  }
}

@Composable
@Preview
private fun GalleryPreview() {
  AutosTheme { Gallery() }
}

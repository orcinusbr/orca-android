/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.feature.gallery.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.mandatorySystemGesturesPadding
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
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.gallery.GalleryViewModel
import br.com.orcinus.orca.feature.gallery.ui.page.Page
import br.com.orcinus.orca.feature.gallery.ui.page.SampleEntrypoint
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

const val GalleryPagerTag = "gallery-pager"

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun Gallery(modifier: Modifier = Modifier) {
  SampleGallery(modifier)
}

@Composable
internal fun Gallery(
  viewModel: GalleryViewModel,
  entrypointIndex: Int,
  secondary: List<Attachment>,
  onComment: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
  entrypoint: @Composable (Modifier) -> Unit
) {
  val statsDetails by viewModel.statsDetailsFlow.collectAsState(StatsDetails.Empty)

  Gallery(
    entrypointIndex,
    secondary,
    onDownload = viewModel::download,
    statsDetails,
    onComment,
    onFavorite = viewModel::toggleFavorite,
    onRepost = viewModel::toggleRepost,
    onShare = viewModel::share,
    onClose,
    modifier,
    entrypoint
  )
}

@Composable
internal fun SampleGallery(
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
    StatsDetails.createSample(
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    ),
    onComment,
    onFavorite,
    onRepost,
    onShare,
    onClose,
    modifier
  ) {
    SampleEntrypoint(it)
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
  entrypoint: @Composable (Modifier) -> Unit
) {
  var areActionsVisible by rememberSaveable { mutableStateOf(true) }
  var areOptionsVisible by rememberSaveable(areActionsVisible) { mutableStateOf(false) }
  val pagerState =
    rememberPagerState(initialPage = entrypointIndex, pageCount = secondary.size::inc)
  val pages =
    remember(entrypointIndex, secondary) {
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

  Box(modifier.background(Color.Black).mandatorySystemGesturesPadding().fillMaxHeight()) {
    HorizontalPager(
      pagerState,
      Modifier.fillMaxHeight().testTag(GalleryPagerTag),
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

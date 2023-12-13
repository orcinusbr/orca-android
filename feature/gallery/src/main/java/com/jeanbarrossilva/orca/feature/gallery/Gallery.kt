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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatsDetails
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

internal const val GALLERY_PAGER_TAG = "gallery-pager-tag"

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun Gallery(
  primaryIndex: Int,
  secondary: List<Attachment>,
  onDownload: () -> Unit,
  statsDetails: StatsDetails,
  onComment: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
  primary: @Composable (Modifier, Sizing) -> Unit
) {
  var areActionsVisible by rememberSaveable { mutableStateOf(true) }
  var areOptionsVisible by rememberSaveable(areActionsVisible) { mutableStateOf(false) }
  val pagerState = rememberPagerState(pageCount = secondary.size::inc)
  val sizing = remember { Sizing.Widened }
  val pages =
    remember(primaryIndex, secondary, sizing) {
      List<@Composable () -> Unit>(pagerState.pageCount) { index ->
        @Composable {
          val coroutineScope = rememberCoroutineScope()
          val zoomState = rememberZoomState()
          val isZoomedIn by zoomState.isZoomedInAsState
          val pageModifier =
            Modifier.zoomable(
                zoomState,
                onTap = {
                  areActionsVisible = isZoomedIn
                  if (isZoomedIn) {
                    coroutineScope.launch { zoomState.reset() }
                  }
                },
                onDoubleTap = {
                  areActionsVisible = isZoomedIn
                  zoomState.toggleScale(2.5f, position = it)
                }
              )
              .animateContentSize()
          if (index == primaryIndex) {
            primary(pageModifier, sizing)
          } else {
            Image(
              rememberImageLoader(secondary[index - if (index < primaryIndex) 0 else 1].url),
              contentDescription = stringResource(R.string.feature_gallery_attachment, index.inc()),
              pageModifier,
              sizing
            )
          }
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
    primaryIndex = 0,
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
    Image(
      rememberImageLoader(com.jeanbarrossilva.orca.std.imageloader.compose.R.drawable.image),
      contentDescription = stringResource(R.string.feature_gallery_attachment, 1),
      pageModifier,
      sizing
    )
  }
}

@Composable
@Preview
private fun GalleryPreview() {
  AutosTheme { Gallery() }
}

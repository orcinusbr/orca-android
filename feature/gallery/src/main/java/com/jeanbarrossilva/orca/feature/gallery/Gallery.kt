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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

internal const val GALLERY_PAGER_TAG = "gallery-pager-tag"

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun Gallery(
  primaryIndex: Int,
  secondary: List<Attachment>,
  modifier: Modifier = Modifier,
  primary: @Composable (Modifier, Sizing) -> Unit
) {
  val pagerState = rememberPagerState(pageCount = secondary.size::inc)
  val sizing = remember { Sizing.Widened }
  val pages =
    remember(primaryIndex, secondary, sizing) {
      List<@Composable () -> Unit>(pagerState.pageCount) { index ->
        @Composable {
          val zoomState = rememberZoomState()
          val pageModifier = Modifier.zoomable(zoomState)
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

  Scaffold(modifier, containerColor = Color.Black) {
    HorizontalPager(
      pagerState,
      Modifier.fillMaxHeight().testTag(GALLERY_PAGER_TAG),
      beyondBoundsPageCount = 1,
      verticalAlignment = Alignment.CenterVertically
    ) { page ->
      pages[page]()
    }
  }
}

@Composable
internal fun Gallery(modifier: Modifier = Modifier) {
  Gallery(primaryIndex = 0, Attachment.samples, modifier) { pageModifier, sizing ->
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

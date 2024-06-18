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

package br.com.orcinus.orca.feature.gallery.ui.page

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.feature.gallery.R
import br.com.orcinus.orca.feature.gallery.ui.isZoomedInAsState
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.std.image.compose.rememberImageLoader
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun Page(modifier: Modifier = Modifier) {
  Page(
    entrypointIndex = 0,
    currentIndex = 1,
    secondary = Attachment.samples,
    onActionsVisibilityToggle = {},
    modifier
  ) {
    SampleEntrypoint(it)
  }
}

@Composable
internal fun Page(
  entrypointIndex: Int,
  currentIndex: Int,
  secondary: List<Attachment>,
  onActionsVisibilityToggle: (areActionsVisible: Boolean) -> Unit,
  modifier: Modifier = Modifier,
  entrypoint: @Composable (Modifier) -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val zoomState = rememberZoomState()
  val isZoomedIn by zoomState.isZoomedInAsState
  val pageModifier =
    modifier
      .zoomable(
        zoomState,
        onTap = {
          onActionsVisibilityToggle(isZoomedIn)
          if (isZoomedIn) {
            coroutineScope.launch { zoomState.reset() }
          }
        },
        onDoubleTap = {
          onActionsVisibilityToggle(isZoomedIn)
          zoomState.toggleScale(2.5f, position = it)
        }
      )
      .animateContentSize()
      .semantics { index = currentIndex }
  if (currentIndex == entrypointIndex) {
    entrypoint(pageModifier)
  } else {
    rememberImageLoader(secondary[currentIndex - if (currentIndex < entrypointIndex) 0 else 1].uri)
      .load()(
      stringResource(R.string.feature_gallery_attachment, currentIndex.inc()),
      RectangleShape,
      ContentScale.FillWidth,
      pageModifier
    )
  }
}

@Composable
internal fun SampleEntrypoint(modifier: Modifier = Modifier) {
  rememberImageLoader(br.com.orcinus.orca.platform.core.R.drawable.sample_cover_default).load()(
    stringResource(R.string.feature_gallery_attachment, 1),
    RectangleShape,
    ContentScale.FillWidth,
    modifier
  )
}

@Composable
@Preview
private fun PagePreview() {
  AutosTheme { Page() }
}

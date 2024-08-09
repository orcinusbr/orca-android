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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.gallery.R
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.menu.DropdownMenu
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

internal const val GalleryActionsOptionsButtonTag = "gallery-actions-options-button"
internal const val GalleryActionsOptionsDownloadItemTag = "gallery-actions-options-download-action"
internal const val GalleryActionsOptionsMenuTag = "gallery-actions-options-menu"
internal const val GalleryActionsTag = "gallery-actions"

const val GalleryActionsCloseButtonTag = "gallery-actions-close-button"

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
fun Actions(modifier: Modifier = Modifier, areOptionsVisible: Boolean = false) {
  Actions(
    areOptionsVisible,
    onOptionsVisibilityToggle = {},
    onDownload = {},
    StatsDetails.createSample(
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    ),
    onComment = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClose = {},
    modifier
  )
}

@Composable
internal fun Actions(
  areOptionsVisible: Boolean,
  onOptionsVisibilityToggle: (areOptionsVisible: Boolean) -> Unit,
  onDownload: () -> Unit,
  details: StatsDetails,
  onComment: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier.padding(AutosTheme.spacings.medium.dp).fillMaxHeight().testTag(GalleryActionsTag),
    Arrangement.SpaceBetween
  ) {
    CompositionLocalProvider(LocalContentColor provides Color.White) {
      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        HoverableIconButton(onClick = onClose, Modifier.testTag(GalleryActionsCloseButtonTag)) {
          Icon(
            AutosTheme.iconography.close.asImageVector,
            contentDescription = stringResource(R.string.feature_gallery_close)
          )
        }

        Box {
          HoverableIconButton(
            onClick = { onOptionsVisibilityToggle(true) },
            Modifier.testTag(GalleryActionsOptionsButtonTag)
          ) {
            Icon(
              AutosTheme.iconography.expand.asImageVector,
              contentDescription = stringResource(R.string.feature_gallery_options)
            )
          }

          DropdownMenu(
            areOptionsVisible,
            onDismissal = { onOptionsVisibilityToggle(false) },
            Modifier.testTag(GalleryActionsOptionsMenuTag)
          ) {
            DropdownMenuItem(
              text = { Text(stringResource(R.string.feature_gallery_download)) },
              onClick = {
                onDownload()
                onOptionsVisibilityToggle(false)
              },
              Modifier.testTag(GalleryActionsOptionsDownloadItemTag),
              leadingIcon = {
                Icon(
                  AutosTheme.iconography.download.asImageVector,
                  contentDescription = stringResource(R.string.feature_gallery_download)
                )
              }
            )
          }
        }
      }

      br.com.orcinus.orca.composite.timeline.stat.Stats(
        details,
        onComment,
        onFavorite,
        onRepost,
        onShare,
        Modifier.fillMaxWidth(),
        LocalContentColor.current
      )
    }
  }
}

@Composable
@Preview
private fun ActionsPreview() {
  AutosTheme { Actions() }
}

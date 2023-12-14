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
import com.jeanbarrossilva.orca.feature.gallery.R
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.kit.menu.DropdownMenu
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.Stats
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatsDetails

internal const val GALLERY_ACTIONS_CLOSE_BUTTON_TAG = "gallery-actions-close-button"
internal const val GALLERY_ACTIONS_OPTIONS_BUTTON_TAG = "gallery-actions-options-button"
internal const val GALLERY_ACTIONS_OPTIONS_DOWNLOADS_ITEM_TAG =
  "gallery-actions-options-download-action"
internal const val GALLERY_ACTIONS_OPTIONS_MENU_TAG = "gallery-actions-options-menu"
internal const val GALLERY_ACTIONS_TAG = "gallery-actions"

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
    modifier.padding(AutosTheme.spacings.medium.dp).fillMaxHeight().testTag(GALLERY_ACTIONS_TAG),
    Arrangement.SpaceBetween
  ) {
    CompositionLocalProvider(LocalContentColor provides Color.White) {
      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        HoverableIconButton(onClick = onClose, Modifier.testTag(GALLERY_ACTIONS_CLOSE_BUTTON_TAG)) {
          Icon(
            AutosTheme.iconography.close.asImageVector,
            contentDescription = stringResource(R.string.feature_gallery_close)
          )
        }

        Box {
          HoverableIconButton(
            onClick = { onOptionsVisibilityToggle(true) },
            Modifier.testTag(GALLERY_ACTIONS_OPTIONS_BUTTON_TAG)
          ) {
            Icon(
              AutosTheme.iconography.expand.asImageVector,
              contentDescription = stringResource(R.string.feature_gallery_options)
            )
          }

          DropdownMenu(
            areOptionsVisible,
            onDismissal = { onOptionsVisibilityToggle(false) },
            Modifier.testTag(GALLERY_ACTIONS_OPTIONS_MENU_TAG)
          ) {
            DropdownMenuItem(
              text = { Text(stringResource(R.string.feature_gallery_download)) },
              onClick = {
                onDownload()
                onOptionsVisibilityToggle(false)
              },
              Modifier.testTag(GALLERY_ACTIONS_OPTIONS_DOWNLOADS_ITEM_TAG),
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

      Stats(
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
internal fun Actions(modifier: Modifier = Modifier, areOptionsVisible: Boolean = false) {
  Actions(
    areOptionsVisible,
    onOptionsVisibilityToggle = {},
    onDownload = {},
    StatsDetails.sample,
    onComment = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClose = {},
    modifier
  )
}

@Composable
@Preview
private fun ActionsPreview() {
  AutosTheme { Actions() }
}

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

package br.com.orcinus.orca.composite.timeline.search

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import br.com.orcinus.orca.composite.timeline.search.field.Unpadded
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [Searchable]'s content for testing purposes. */
@VisibleForTesting internal const val ContentTag = "searchable-content"

/**
 * Layout that can replace a slot by a [SearchTextField].
 *
 * @param fillerColor [Color] by which the container that fills the space previously occupied by the
 *   content replaced by the [SearchTextField] is colored.
 * @param modifier [Modifier] applied to the underlying [Box].
 * @param searchTextFieldOffset Amount in both axes by which the [SearchTextField] is offset.
 * @param searchTextFieldPadding Space to surround the [SearchTextField] with.
 * @param content Content to be shown.
 */
@Composable
fun Searchable(
  fillerColor: Color,
  modifier: Modifier = Modifier,
  searchTextFieldOffset: DpOffset = DpOffset.Zero,
  searchTextFieldPadding: PaddingValues = Unpadded,
  content: @Composable SearchableScope.() -> Unit
) {
  val isReplaceableComposedState = remember { mutableStateOf(false) }

  Box(modifier.testTag(ContentTag)) {
    remember(content, fillerColor) {
        SearchableScope(
          searchTextFieldOffset,
          searchTextFieldPadding,
          isReplaceableComposedState,
          fillerColor
        )
      }
      .content()
  }
}

/**
 * Layout that can replace a slot by a [SearchTextField].
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param modifier [Modifier] applied to the underlying [Box].
 * @param searchTextFieldOffset Amount in both axes by which the [SearchTextField] is offset.
 * @param searchTextFieldPadding Space to surround the [SearchTextField] with.
 * @param content Content to be shown.
 */
@Composable
@VisibleForTesting
internal fun Searchable(
  modifier: Modifier = Modifier,
  searchTextFieldOffset: DpOffset = DpOffset.Zero,
  searchTextFieldPadding: PaddingValues = Unpadded,
  content: @Composable SearchableScope.() -> Unit
) {
  Searchable(
    fillerColor = Color.Transparent,
    modifier,
    searchTextFieldOffset,
    searchTextFieldPadding,
    content
  )
}

/** Preview of a [Searchable]. */
@Composable
@MultiThemePreview
private fun SearchablePreview() {
  AutosTheme {
    Searchable {
      Replaceable {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(
          title = { Text("Content") },
          actions = {
            HoverableIconButton(onClick = ::show) {
              Icon(
                AutosTheme.iconography.search.asImageVector,
                stringResource(
                  br.com.orcinus.orca.platform.autos.R.string
                    .platform_autos_search_content_description
                )
              )
            }
          }
        )
      }
    }
  }
}
